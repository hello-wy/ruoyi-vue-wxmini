# GET /wxmini/pay/payroll/orders/{orderNo}

## 用途

- `GET`：查询当前商家的工资支付订单状态与明细。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "orderNo": "PAYROLL20260502123456",
    "jobId": 1001,
    "jobTitle": "活动协助兼职",
    "totalAmount": 200.00,
    "status": 1,
    "payTime": "2026-05-02 15:30:00",
    "createTime": "2026-05-02 15:20:00",
    "items": [
      {
        "employeeUserId": 2001,
        "employeeName": "张三",
        "phoneMasked": "138****8000",
        "hours": 8.00,
        "hourlyRate": 25.00,
        "amount": 200.00,
        "status": 1
      }
    ]
  }
}
```

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxPayController.java`
