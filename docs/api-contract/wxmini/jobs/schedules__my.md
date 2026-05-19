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
      "status": 1,
      "attendanceStatus": 0,
      "attendanceStatusLabel": "未签到",
      "signImageUrl": "/profile/job-sign/321/1715832000000.jpg",
      "auditStatus": 1,
      "auditRemark": null,
      "signTime": null
    }
  ]
}
```

## 字段说明

- `orderNo`：报名订单号，提交签到图片时使用。
- `attendanceStatus` / `attendanceStatusLabel`：后台签到状态。
- `signImageUrl`：用户已上传的签到图片相对路径；未上传时为空。
- `auditStatus`：签到图片审核状态，`0` 未提交，`1` 待审核，`2` 已通过，`3` 已驳回。
- `auditRemark`：审核备注。
- `signTime`：签到通过或记录签到的时间。

## 失败场景或特殊说明

- 仅返回当前登录用户已支付报名订单关联的岗位安排。
- 不返回未支付、取消、退款订单。
- 兼职签到、签到图审核结果按报名订单隔离；同一岗位退款后重新报名，会生成新的订单视图，不复用历史订单的签到结果。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxJobScheduleController.java`
