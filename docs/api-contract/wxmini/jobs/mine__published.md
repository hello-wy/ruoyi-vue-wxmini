# GET /wxmini/jobs/mine/published

## 用途

- `GET`：获取当前商家自己发布的兼职日结岗位，用于“兼职日结查询”页面。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。
- 仅 `userType = 2` 的商家身份可访问。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "id": 1001,
      "title": "活动协助兼职",
      "salaryDay": 200.00,
      "workDate": "2026-05-03",
      "workTime": "09:00-18:00",
      "location": "南京市鼓楼区xx广场",
      "status": 0,
      "signupLimit": 10,
      "paidSignupCount": 3,
      "payrollReminder": true,
      "settlementStatus": "NONE",
      "settlementStatusLabel": "不可结账",
      "canSettle": false,
      "canCancel": true,
      "canResumeRecruiting": false
    }
  ]
}
```

## 失败场景或特殊说明

- 非商家身份返回业务错误：`仅商家可查看兼职日结查询`。
- 只返回当前商家作为 `publisherUid` 发布的岗位。
- `paidSignupCount` 只统计已支付报名订单。
- `payrollReminder = true` 表示岗位工作日期已到或已过。
- `settlementStatus` 取值：
  - `NONE`：不可结账
  - `PENDING`：待结账
  - `SETTLED`：已结清
- 只有岗位状态为 `已满员(1)` 或 `已取消(3)` 且工时已结束后，才会进入结账状态计算。
- 若还有未发工资的已支付报名员工，则 `settlementStatus = PENDING`；否则为 `SETTLED`。
- `canCancel` 仅在岗位处于 `招聘中(0)` 时为 `true`。
- `canResumeRecruiting` 仅在岗位处于 `已取消(3)` 时为 `true`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxJobScheduleController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxJobScheduleServiceImpl.java`
