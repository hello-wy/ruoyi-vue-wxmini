# POST /wxmini/pay/notify

## 用途

微信支付异步回调，由微信服务器发起。

## 鉴权

- 公开接口
- 不走前端调用链路

## 请求头

- `Wechatpay-Serial`
- `Wechatpay-Signature`
- `Wechatpay-Nonce`
- `Wechatpay-Timestamp`

## Body

- 微信支付回调 JSON 报文

## 响应

- 成功返回 XML：`<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>`
- 失败返回 XML：`<xml><return_code><![CDATA[FAIL]]></return_code></xml>`

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxPayController.java`
