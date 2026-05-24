# POST /system/tutoring-admin/schedules/{id}/audit

## 用途

- `POST`：后台审核家教课表，通过后生成家教结算单，并立即按扣佣后的课酬入账到伴学管钱包。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:record:edit')`。

## 请求头

- `Authorization: Bearer <token>`
- `Content-Type: application/json`

## Path 参数

- `id`：课表 ID。

## Body 示例

```json
{
  "targetStatus": 2,
  "remark": "确认课时无误，自动发放课酬"
}
```

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

## 失败场景或特殊说明

- 课表不存在：`msg = 课表不存在`。
- 当前仅支持审核为 `2=待结算`，否则返回：`仅支持审核为待结算`。
- 课表未处于 `status = 1` 的正在上课/待管理员审核状态：`msg = 当前课表状态不可审核`。
- 家长尚未确认完课：`msg = 家长尚未确认完课`。
- 同一课表若已存在 `tutoring_payroll_item`，则不会重复生成结算单。
- 审核成功后会生成 `walletBizId = TUTORING_PAYROLL:{scheduleId}`，并调用钱包入账能力发放 `netAmount`。
- 发放成功后，结算单状态更新为 `1=已发放`，课表状态推进到 `3=已结算`。
- 钱包流水 `bizType = PAYROLL`，`remark = 家教课酬入账`。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/TutoringAdminController.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/TutoringBindingServiceSupport.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/TutoringSettlementServiceImpl.java`
