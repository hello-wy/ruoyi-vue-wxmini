-- =============================================================================
-- File   : 2026-signup-users-jobid14-data-fix.sql
-- Spec   : .kiro/specs/signup-users-page-fix · Task 3.1 (A1 · DataPresence)
-- Scope  : 仅当 Task 1 探索结论 A1·DataPresence 在生产/真实库人工核查后
--          被确认命中（jobId=14 应有已支付报名却 count(status=1)=0）时启用。
-- Status : OPS ARTIFACT · MANUAL_VERIFICATION_REQUIRED
--
-- 背景
--   Task 1 (WxJobScheduleSignupUsersExplorationTest) 在本地无法直连生产库，
--   A1 暂记为 MANUAL_VERIFICATION_REQUIRED。本脚本提供一套「先 dry-run，再
--   条件性 update，再保留回滚」的标准流程；A3 (SQL inner join → left join)
--   作为结构性根因独立修复，与本脚本正交。
--
-- 前置条件 (Preconditions) ── 必须全部满足后才能执行 §B Update
--   P0. 已在生产/灰度库以只读账号执行 §A Dry-run，并把结果存档到本仓库
--       同目录的 2026-signup-users-jobid14-data-fix.report.txt。
--   P1. §A.1 的 count(status=1) 实测为 0（或显著少于业务预期值）。
--   P2. §A.2 列出的 status ∈ {0, 2} 候选行经业务（产品 + 商家工单）逐行
--       核对，确认它们「实际已支付完成」属于状态错位，而非历史退款 / 未付。
--   P3. 已与 DBA 双人复核：备份 job_signup_order 全量，或至少导出本次涉及
--       order_no 的 before-image 到 §C 的回滚模板。
--   P4. 已在非生产库回放 §B Update + §C Rollback 一遍，行数 / 影响范围与
--       预期一致。
--   P5. 变更窗口已与商家通知（避免「下架岗位 → 重新上架」与本脚本并发）。
--   若任何一项不满足，禁止执行 §B Update。
--
-- 影响范围
--   仅 job_signup_order 一张表。不动 user_info / sign_in_record /
--   wx_user_profile / payroll_*。
--
-- 与代码层修复的关系
--   - A3 (selectPaidSignupUsersByJobId 的 inner join → left join) 是结构性
--     bug，无论本脚本是否执行都必须修。
--   - 本脚本仅修「数据错位」这一条 A1 路径，且仅在人工核查通过后启用。
--
-- 执行顺序：A (Dry-run) → 人工评审 → B (Update) → 验证 → 必要时 C (Rollback)
-- =============================================================================


-- -----------------------------------------------------------------------------
-- §A · Dry-run 只读核查（必须先跑，结果存档）
-- -----------------------------------------------------------------------------

-- A.1 当前 jobId=14 已支付订单数 (status=1)
--     若结果为 0，进入 A.2；若 > 0，停止本脚本，回到 A3 / A4 候选定位。
select count(*) as paid_count
from job_signup_order
where job_id = 14
  and status = 1;

-- A.2 列出 jobId=14 当前所有非「已支付」候选行，便于业务比对
--     重点关注 status ∈ (0, 2) 中实际已完成支付（如微信回调记录到账但
--     单据未刷状态）的脏数据。
select id,
       order_no,
       user_id,
       status,
       pay_amount,
       pay_time,
       create_time,
       update_time
from job_signup_order
where job_id = 14
  and status in (0, 2)
order by pay_time desc, id desc;

-- A.3 候选行明细 + 对应 user_info 是否存在（辅助判断 A1 vs A3）
--     若 user_info 为空 → A3 候选（结构性 SQL bug，本脚本不修）。
--     若 user_info 完整但 status 错位 → A1 候选，可进入 §B Update。
select jso.id          as order_id,
       jso.order_no,
       jso.status      as current_status,
       jso.user_id,
       jso.pay_amount,
       jso.pay_time,
       ui.id           as user_info_id,
       ui.user_name,
       ui.phone
from job_signup_order jso
         left join user_info ui on ui.user_id = jso.user_id
where jso.job_id = 14
  and jso.status in (0, 2)
order by jso.pay_time desc, jso.id desc;


-- -----------------------------------------------------------------------------
-- §B · 条件性 Update 模板（人工核查通过后逐行启用）
-- -----------------------------------------------------------------------------
-- 使用方法
--   1. 把 §A.3 的输出粘到工单/评审纪要，业务勾选实际「已完成支付」的 order_no。
--   2. 把勾选结果填入下面的 IN(...) 列表（逐个 order_no，禁止使用范围条件）。
--   3. 在事务中执行；先 select 校验受影响行，再 update，最后 commit。
--   4. 受影响行数必须等于工单勾选数；不一致立刻回滚。
--   5. 不允许用 status in (0,2) 一刀切批量更新；必须按 order_no 白名单。
-- -----------------------------------------------------------------------------

-- B.0 在事务内执行 (示例为 MySQL；其它方言请按 DBA 规范调整)
-- start transaction;

-- B.1 预校验：把白名单订单当前状态打印到控制台，确认仍是 0/2，未被并发改动
-- select id, order_no, status, pay_time, update_time
-- from job_signup_order
-- where job_id = 14
--   and order_no in (
--       'REPLACE_WITH_ORDER_NO_1',
--       'REPLACE_WITH_ORDER_NO_2'
--       -- ... 由人工核查结果填充 ...
--   )
-- for update;

-- B.2 真正的修复 update（仅当 B.1 行数与白名单一致时执行）
-- update job_signup_order
--    set status      = 1,
--        update_time = now(),
--        remark      = concat(ifnull(remark, ''),
--                             ' | ops-fix:2026-signup-users-jobid14 status->1')
--  where job_id = 14
--    and status in (0, 2)
--    and order_no in (
--        'REPLACE_WITH_ORDER_NO_1',
--        'REPLACE_WITH_ORDER_NO_2'
--        -- ... 由人工核查结果填充 ...
--    );

-- B.3 后置校验：确认 §A.1 现在 > 0 且等于白名单数量
-- select count(*) as paid_count_after
-- from job_signup_order
-- where job_id = 14
--   and status = 1;

-- B.4 提交或回滚
-- commit;        -- 校验通过后提交
-- rollback;     -- 任何异常立即回滚


-- -----------------------------------------------------------------------------
-- §C · 回滚模板（生效后保留至少 7 天，验证商家端无副作用后再归档）
-- -----------------------------------------------------------------------------
-- 使用方法
--   1. 在 §B 执行前，先把白名单的 (id, order_no, status_before) 三元组导出
--      存档到 2026-signup-users-jobid14-data-fix.before-image.csv。
--   2. 一旦商家或客服反馈本次变更引发副作用（如把退款单错改为已支付），
--      用 §C 的 update 把 status 还原到 status_before。
--   3. 回滚同样必须按 order_no 白名单逐行，禁止反向一刀切。
-- -----------------------------------------------------------------------------

-- C.0 在事务内执行
-- start transaction;

-- C.1 把每一笔订单的 status 恢复为 §B 执行前的快照值
--     注意：status_before 应来自 before-image.csv，不是本脚本硬编码。
--     下面给出两种最常见的还原形态作为示例。

-- C.1.a 单条订单回滚（最稳，每条订单一条 update）
-- update job_signup_order
--    set status      = 0,                       -- 还原为 status_before
--        update_time = now(),
--        remark      = concat(ifnull(remark, ''),
--                             ' | ops-rollback:2026-signup-users-jobid14 status->0')
--  where job_id = 14
--    and order_no = 'REPLACE_WITH_ORDER_NO_1'
--    and status   = 1;                          -- 仅回滚那些当前确为 1 的行

-- C.1.b 批量回滚（仅当 before-image 全部 status_before 相同，例如统一为 0）
-- update job_signup_order
--    set status      = 0,
--        update_time = now(),
--        remark      = concat(ifnull(remark, ''),
--                             ' | ops-rollback:2026-signup-users-jobid14 status->0')
--  where job_id = 14
--    and status   = 1
--    and order_no in (
--        'REPLACE_WITH_ORDER_NO_1',
--        'REPLACE_WITH_ORDER_NO_2'
--        -- ... 与 §B 完全一致的白名单 ...
--    );

-- C.2 回滚后校验
-- select count(*) as paid_count_after_rollback
-- from job_signup_order
-- where job_id = 14
--   and status = 1;

-- C.3 提交或回滚
-- commit;
-- rollback;


-- -----------------------------------------------------------------------------
-- §D · 执行后归档要求
-- -----------------------------------------------------------------------------
--   D.1 把 §A 的 dry-run 结果、§B 的实际受影响行数、commit / rollback 时点
--       记录到 .kiro/specs/signup-users-page-fix/exploration-result.md
--       的 A1 节，标注从 MANUAL_VERIFICATION_REQUIRED → HIT/NOT_HIT。
--   D.2 商家在前端二次复现：展开 jobId=14 的「已支付报名人员」面板，确认
--       列表非空且字段 (displayName / phoneMasked / orderNo) 齐备。
--   D.3 把本脚本与 before-image.csv 一起归档到运维变更工单。
-- =============================================================================
