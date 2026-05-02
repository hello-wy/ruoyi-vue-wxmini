# GET /wxmini/jobs/schedules/my

## 用途

- `GET`：获取当前登录兼职用户的工作安排。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "jobId": 1001,
      "orderNo": "JOB202605020001",
      "title": "活动协助兼职",
      "workDate": "2026-05-03 00:00:00",
      "workTime": "09:00-18:00",
      "location": "南京市鼓楼区xx广场",
      "salaryDay": 200.00,
      "status": 1
    }
  ]
}
```

## 失败场景或特殊说明

- 仅返回当前登录用户已支付报名订单关联的岗位安排。
- 不返回未支付、取消、退款订单。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxJobScheduleController.java`
