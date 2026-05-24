# POST /system/tutoring-admin/payroll/batch-pay

## 用途

- `POST`：后台批量发放家教课酬，复用钱包入账能力。
- 家教签到审核通过后会自动发放课酬；该接口主要用于处理历史遗留或人工补发的待发放结算单。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:wallet:withdraw')`。

## 请求头

- `Authorization: Bearer <token>`
- `Content-Type: application/json`

## Body 示例

```json
{
  "itemIds": [70001, 70002]
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

- 未选择待发放结算单：`msg = 请选择待发放结算单`。
- 结算单不存在：`msg = 结算单不存在`。
- 对已发放状态的结算单会直接跳过，不会重复调用 `IWalletService.creditPayroll`。
- 钱包业务幂等依赖 `tutoring_payroll_item.status` 与 `walletBizId`，避免重复写入 `wallet_transaction`。
- 发放成功后，会把对应课表状态推进到 `3=已结算`。
- 发放金额为结算单 `netAmount`，即扣佣后的课酬。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/TutoringAdminController.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/TutoringSettlementServiceImpl.java`
