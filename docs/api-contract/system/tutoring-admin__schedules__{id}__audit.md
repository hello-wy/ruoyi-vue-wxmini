# POST /system/tutoring-admin/schedules/{id}/audit

## 用途

- `POST`：后台审核家教课表，通过后把课表推进到待结算，并生成家教结算单。

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
  "remark": "确认课时无误，进入待结算"
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
- 课表未处于待家长确认状态：`msg = 当前课表状态不可审核`。
- 家长尚未确认完课：`msg = 家长尚未确认完课`。
- 同一课表若已存在 `tutoring_payroll_item`，则不会重复生成结算单。
- 审核成功后会生成 `walletBizId = TUTORING_PAYROLL:{scheduleId}`，供后续钱包发放幂等复用。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/TutoringAdminController.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/TutoringBindingServiceSupport.java`
