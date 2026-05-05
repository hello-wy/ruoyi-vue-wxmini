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
- 订单状态不允许退款时返回错误消息：`订单状态不允许退款`。
- 微信退款接口调用失败时返回对应错误消息。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/RefundController.java`
