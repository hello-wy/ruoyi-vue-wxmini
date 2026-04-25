# GET /wxmini/pay/salon/orders/{orderNo}

## 用途

- `GET`：查询沙龙支付订单状态（需登录）。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

## Path 参数

- `orderNo`：路径参数。

## GET 请求

### Query 参数

- 无。

### Body 示例

- 无。

### 成功响应示例

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

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 当前订单状态仍为 `PENDING` 时，接口会先主动向微信查单并同步本地状态。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxPayController.java`
