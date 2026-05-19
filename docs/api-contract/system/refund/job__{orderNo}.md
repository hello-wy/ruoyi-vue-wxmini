# POST /system/refund/job/{orderNo}

## 用途

- 对指定兼职订单发起后台管理员退款。

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
  "msg": "退款成功"
}
```

## 失败场景或特殊说明

- 订单不存在时返回错误消息：`订单不存在`。
- 未通过签到审核时返回错误消息：`签到审核通过后才可退款`。
- 微信退款接口调用失败时返回对应错误消息。
- 该接口内部会先执行签到退款资格校验，再调用微信退款。
- 退款资格只基于当前订单的签到审核结果判断，不复用同岗位其他订单的审核状态。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/RefundController.java`
