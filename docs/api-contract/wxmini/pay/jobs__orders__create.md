# POST /wxmini/pay/jobs/orders/create

## 用途

- `POST`：创建兼职报名支付订单，返回 JSAPI 支付参数（需登录）。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

## Path 参数

- 无。

## POST 请求

### Query 参数

- 无。

### Body 示例

```json
{
  "jobId": 1
}
```

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "orderNo": "JOBSIGN202604161200001234",
    "payParam": {
      "appId": "wx123",
      "timeStamp": "1710000000",
      "nonceStr": "nonce",
      "packageValue": "prepay_id=wx123",
      "paySign": "sign"
    }
  }
}
```

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 若当前用户同岗位存在旧 `PENDING` 订单，服务会先关闭旧订单再创建新订单。
- 微信支付商品描述使用 `兼职押金：{岗位标题}`，例如 `兼职押金：日结兼职助教`。
- 用户实名认证姓名或身份证缺失、白名单未命中、或白名单姓名与实名认证姓名不一致时，返回：`请你联系管理员开通权限进行报名`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxPayController.java`
