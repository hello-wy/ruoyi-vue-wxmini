# GET /wxmini/pay/salon/orders/{orderNo}

## 用途

查询当前登录用户的沙龙支付订单状态与展示信息。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`

## Path 参数

- `orderNo`：必填，平台订单号

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "orderNo": "SALON202604141234561712345678901",
    "title": "组局思维",
    "amount": 128.00,
    "payTime": "2026-04-14 12:35:10",
    "status": "PAID"
  }
}
```

## 说明

- 仅允许查询当前登录用户自己的订单
- 前端只有在 `status=PAID` 时才能跳转到订单详情页
