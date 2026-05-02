# POST /wxmini/pay/payroll/orders/create

## 用途

- `POST`：商家创建岗位工资支付订单，返回微信 JSAPI 支付参数。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。
- 仅岗位发布商家可创建。

## Body 示例

```json
{
  "jobId": 1001,
  "employees": [
    {
      "employeeUserId": 2001,
      "hours": "8",
      "hourlyRate": "25"
    }
  ]
}
```

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "orderNo": "PAYROLL20260502123456",
    "payParam": {
      "appId": "wx123",
      "timeStamp": "1714620000",
      "nonceStr": "nonce",
      "packageValue": "prepay_id=xxx",
      "signType": "RSA",
      "paySign": "sign"
    }
  }
}
```

## 失败场景或特殊说明

- 服务端会重新计算总金额，不信任前端传值。
- 员工必须属于该岗位已支付报名用户。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxPayController.java`
