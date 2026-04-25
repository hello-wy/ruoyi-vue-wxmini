# POST /wxmini/pay/jobs/notify

## 用途

- `POST`：微信兼职报名支付结果异步回调通知（由微信服务器调用）。

## 鉴权

- 公开接口。
- 仅供微信支付服务端回调，不走前端调用链路。

## 请求头

- `Wechatpay-Serial`
- `Wechatpay-Signature`
- `Wechatpay-Nonce`
- `Wechatpay-Timestamp`
- `Request-ID`：微信回调请求号，服务端用于幂等处理。

## Path 参数

- 无。

## POST 请求

### Query 参数

- 无。

### Body 示例

- 微信支付回调原始报文，由控制器从 `HttpServletRequest` 输入流中读取。

### 成功响应示例

```xml
<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>
```

### 失败场景或特殊说明

- 以当前 controller/service 的实际校验结果为准。
- 岗位满额时，服务会将当前订单收敛为退款流程。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxPayController.java`
