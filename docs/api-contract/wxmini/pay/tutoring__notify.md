# POST /wxmini/pay/tutoring/notify

## 用途

- `POST`：微信家教支付结果异步回调通知，由微信服务器调用。

## 鉴权

- 无业务登录态，依赖微信支付回调签名校验。

## 请求头

- 微信支付平台回调标准头：
  - `Wechatpay-Serial`
  - `Wechatpay-Signature`
  - `Wechatpay-Nonce`
  - `Wechatpay-Timestamp`
  - `Request-ID`

## 成功响应示例

```xml
<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>
```

## 失败场景或特殊说明

- 回调内容解析失败、支付状态非 `SUCCESS`、或业务处理失败时，返回：

```xml
<xml><return_code><![CDATA[FAIL]]></return_code></xml>
```

- 支付成功后，后端会：
  - 把 `tutoring_order` 置为已支付；
  - 更新当前绑定为已下单；
  - 保留同家长的其它陪伴官绑定，后续购买课程包仍可选择；
  - 幂等生成 `tutoring_schedule`，重复回调不会重复插入课表。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxPayController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxTutoringPayServiceImpl.java`
