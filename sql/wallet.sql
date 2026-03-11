-- ==========================================================
-- 商家支付钱包系统 - 数据库表结构
-- 创建时间: 2026-03-10
-- 数据库: MySQL 8.0+
-- ==========================================================

-- ----------------------------------------------------------
-- 1. merchant_wallet 商家钱包账户表
--    每个用户（商家）拥有唯一的钱包账户
-- ----------------------------------------------------------
DROP TABLE IF EXISTS `merchant_wallet`;
CREATE TABLE `merchant_wallet` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT                    COMMENT '主键ID',
    `uid`             BIGINT          NOT NULL                                   COMMENT '用户ID，关联 sys_user.user_id，唯一',
    `balance`         DECIMAL(12, 2)  NOT NULL DEFAULT 0.00                     COMMENT '可用余额（元），严禁使用FLOAT',
    `frozen_amount`   DECIMAL(12, 2)  NOT NULL DEFAULT 0.00                     COMMENT '冻结金额（提现处理中的金额）',
    `total_income`    DECIMAL(12, 2)  NOT NULL DEFAULT 0.00                     COMMENT '累计收入',
    `total_withdraw`  DECIMAL(12, 2)  NOT NULL DEFAULT 0.00                     COMMENT '累计提现',
    `withdraw_account` VARCHAR(64)    DEFAULT NULL                               COMMENT '提现绑定账户（微信openid或银行卡号）',
    `withdraw_type`   TINYINT         NOT NULL DEFAULT 1                         COMMENT '提现方式: 1-微信零钱 2-银行卡',
    `status`          TINYINT         NOT NULL DEFAULT 1                         COMMENT '账户状态: 0-已冻结 1-正常',
    `version`         INT             NOT NULL DEFAULT 0                         COMMENT '乐观锁版本号，防并发超扣',
    `create_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP         COMMENT '创建时间',
    `update_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
                                               ON UPDATE CURRENT_TIMESTAMP       COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_uid` (`uid`)                                                  COMMENT '每个用户只有一个钱包',
    KEY `idx_status` (`status`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='商家钱包账户表';


-- ----------------------------------------------------------
-- 2. wallet_transaction 钱包流水记录表
--    记录所有余额变动（收入、提现、转账、退款等）
-- ----------------------------------------------------------
DROP TABLE IF EXISTS `wallet_transaction`;
CREATE TABLE `wallet_transaction` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT                    COMMENT '主键ID',
    `transaction_no`  VARCHAR(64)     NOT NULL                                   COMMENT '平台流水号，全局唯一',
    `wallet_id`       BIGINT          NOT NULL                                   COMMENT '钱包ID，关联 merchant_wallet.id',
    `uid`             BIGINT          NOT NULL                                   COMMENT '用户ID',
    `type`            TINYINT         NOT NULL                                   COMMENT '流水类型: 1-收入 2-提现 3-转账支出 4-转账收入 5-退款支出 6-退款收入',
    `amount`          DECIMAL(12, 2)  NOT NULL                                   COMMENT '变动金额（正数），单位元',
    `direction`       TINYINT         NOT NULL                                   COMMENT '方向: 1-收入(+) 2-支出(-)',
    `balance_before`  DECIMAL(12, 2)  NOT NULL                                   COMMENT '变动前余额快照',
    `balance_after`   DECIMAL(12, 2)  NOT NULL                                   COMMENT '变动后余额快照',
    `biz_type`        VARCHAR(32)     DEFAULT NULL                               COMMENT '关联业务类型: order-订单 withdraw-提现 transfer-转账 refund-退款',
    `biz_id`          BIGINT          DEFAULT NULL                               COMMENT '关联业务ID',
    `remark`          VARCHAR(256)    DEFAULT NULL                               COMMENT '备注说明',
    `create_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP         COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_transaction_no` (`transaction_no`)                            COMMENT '流水号唯一',
    KEY `idx_uid_type`      (`uid`, `type`)                                      COMMENT '按用户+类型查询流水',
    KEY `idx_wallet_create` (`wallet_id`, `create_time`)                         COMMENT '按钱包+时间分页',
    KEY `idx_biz`           (`biz_type`, `biz_id`)                               COMMENT '按业务反查流水'
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='钱包流水记录表';


-- ----------------------------------------------------------
-- 3. wallet_withdraw 提现申请表
--    记录每笔提现申请的完整生命周期
-- ----------------------------------------------------------
DROP TABLE IF EXISTS `wallet_withdraw`;
CREATE TABLE `wallet_withdraw` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT                    COMMENT '主键ID',
    `withdraw_no`     VARCHAR(64)     NOT NULL                                   COMMENT '提现申请单号，全局唯一',
    `uid`             BIGINT          NOT NULL                                   COMMENT '用户ID',
    `wallet_id`       BIGINT          NOT NULL                                   COMMENT '钱包ID',
    `amount`          DECIMAL(12, 2)  NOT NULL                                   COMMENT '提现金额',
    `withdraw_type`   TINYINT         NOT NULL DEFAULT 1                         COMMENT '提现方式: 1-微信零钱 2-银行卡',
    `withdraw_account` VARCHAR(128)   NOT NULL                                   COMMENT '提现账户（openid或脱敏银行卡号）',
    `status`          TINYINT         NOT NULL DEFAULT 0                         COMMENT '状态: 0-待处理 1-处理中 2-已完成 3-已拒绝 4-失败',
    `fail_reason`     VARCHAR(256)    DEFAULT NULL                               COMMENT '失败或拒绝原因',
    `wx_transfer_id`  VARCHAR(128)    DEFAULT NULL                               COMMENT '微信企业付款单号',
    `idempotency_key` VARCHAR(64)     NOT NULL                                   COMMENT '幂等Key，前端生成UUID',
    `handle_time`     DATETIME        DEFAULT NULL                               COMMENT '处理时间',
    `create_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP         COMMENT '申请时间',
    `update_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
                                               ON UPDATE CURRENT_TIMESTAMP       COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_withdraw_no`       (`withdraw_no`),
    UNIQUE KEY `uk_idempotency_key`   (`idempotency_key`)                        COMMENT '幂等唯一索引，防重复提交',
    KEY `idx_uid_status`  (`uid`, `status`),
    KEY `idx_wallet_id`   (`wallet_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='提现申请表';


-- ----------------------------------------------------------
-- 4. wallet_transfer 商家转账记录表
--    需要 wxmini:wallet:transfer 权限
-- ----------------------------------------------------------
DROP TABLE IF EXISTS `wallet_transfer`;
CREATE TABLE `wallet_transfer` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT                    COMMENT '主键ID',
    `transfer_no`     VARCHAR(64)     NOT NULL                                   COMMENT '转账单号，全局唯一',
    `from_uid`        BIGINT          NOT NULL                                   COMMENT '转出方用户ID（商家）',
    `to_uid`          BIGINT          NOT NULL                                   COMMENT '转入方用户ID',
    `amount`          DECIMAL(12, 2)  NOT NULL                                   COMMENT '转账金额',
    `remark`          VARCHAR(256)    DEFAULT NULL                               COMMENT '转账备注',
    `biz_type`        VARCHAR(32)     DEFAULT NULL                               COMMENT '业务场景: settlement-结算 reward-奖励 other-其他',
    `biz_id`          BIGINT          DEFAULT NULL                               COMMENT '关联业务ID（如订单ID）',
    `status`          TINYINT         NOT NULL DEFAULT 1                         COMMENT '状态: 0-处理中 1-已完成 2-已撤销',
    `idempotency_key` VARCHAR(64)     NOT NULL                                   COMMENT '幂等Key，前端生成UUID',
    `create_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP         COMMENT '转账时间',
    `update_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
                                               ON UPDATE CURRENT_TIMESTAMP       COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_transfer_no`       (`transfer_no`),
    UNIQUE KEY `uk_idempotency_key`   (`idempotency_key`),
    KEY `idx_from_uid`  (`from_uid`),
    KEY `idx_to_uid`    (`to_uid`),
    KEY `idx_biz`       (`biz_type`, `biz_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='商家转账记录表';


-- ----------------------------------------------------------
-- 5. wallet_refund 退款记录表
--    关联原始订单，记录退款全流程
-- ----------------------------------------------------------
DROP TABLE IF EXISTS `wallet_refund`;
CREATE TABLE `wallet_refund` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT                    COMMENT '主键ID',
    `refund_no`       VARCHAR(64)     NOT NULL                                   COMMENT '退款单号，全局唯一',
    `order_id`        BIGINT          NOT NULL                                   COMMENT '原始订单ID，关联 order.id',
    `order_no`        VARCHAR(64)     NOT NULL                                   COMMENT '原始订单号快照',
    `uid`             BIGINT          NOT NULL                                   COMMENT '退款发起用户ID（通常为商家）',
    `refund_uid`      BIGINT          NOT NULL                                   COMMENT '退款接收用户ID（消费者）',
    `refund_amount`   DECIMAL(12, 2)  NOT NULL                                   COMMENT '退款金额（不超过原支付金额）',
    `original_amount` DECIMAL(12, 2)  NOT NULL                                   COMMENT '原始支付金额快照',
    `reason`          VARCHAR(256)    DEFAULT NULL                               COMMENT '退款原因',
    `type`            TINYINT         NOT NULL DEFAULT 1                         COMMENT '退款类型: 1-全额退款 2-部分退款',
    `status`          TINYINT         NOT NULL DEFAULT 0                         COMMENT '状态: 0-处理中 1-退款成功 2-退款失败',
    `fail_reason`     VARCHAR(256)    DEFAULT NULL                               COMMENT '失败原因',
    `wx_refund_id`    VARCHAR(128)    DEFAULT NULL                               COMMENT '微信退款单号',
    `idempotency_key` VARCHAR(64)     NOT NULL                                   COMMENT '幂等Key',
    `create_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP         COMMENT '退款申请时间',
    `update_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
                                               ON UPDATE CURRENT_TIMESTAMP       COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_refund_no`         (`refund_no`),
    UNIQUE KEY `uk_idempotency_key`   (`idempotency_key`),
    KEY `idx_order_id`  (`order_id`),
    KEY `idx_uid`       (`uid`),
    KEY `idx_refund_uid` (`refund_uid`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='退款记录表';
