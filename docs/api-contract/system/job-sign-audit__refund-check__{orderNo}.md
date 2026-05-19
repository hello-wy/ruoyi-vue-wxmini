# GET /system/job-sign-audit/refund-check/{orderNo}

## 用途

- 校验指定兼职订单当前是否允许执行退款。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:refund:edit')`。

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- `orderNo`：必填，兼职订单号。

## Query 参数

- 无。

## Body 示例

- 无。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

## 失败场景或特殊说明

- 订单不存在时返回：`订单不存在`。
- 未通过签到审核时返回：`签到审核通过后才可退款`。
- 退款资格校验仅检查当前订单绑定的签到审核结果，不使用同岗位历史订单的签到状态。
- 该接口仅做校验，不实际触发退款。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/JobSignAuditController.java`
