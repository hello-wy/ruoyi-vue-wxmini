# GET /wxmini/jobs/schedules/my

## 用途

- 查询当前登录兼职用户的工作安排，以及对应签到提交与审核状态。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

## Path 参数

- 无。

## Query 参数

- 无。

## Body 示例

- 无。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "jobId": 12,
      "orderNo": "JOB202605130001",
      "title": "周末活动协助",
      "workDate": "2026-05-18",
      "workTime": "09:00-18:00",
      "location": "深圳南山",
      "salaryDay": 180.00,
      "status": 1,
      "attendanceStatus": 0,
      "attendanceStatusLabel": "未签到",
      "signTime": "2026-05-13 09:20:00",
      "signImageUrl": "/profile/sign/job/12/20260513_xxx.png",
      "auditStatus": 1,
      "auditStatusLabel": "待审核",
      "auditRemark": null,
      "canUploadSignImage": false
    }
  ]
}
```

## 失败场景或特殊说明

- 未登录时返回：`请先登录`。
- `auditStatus` 约定：`0=未提交`、`1=待审核`、`2=已通过`、`3=已驳回`。
- `attendanceStatusLabel` 为后端聚合出的签到状态文案。
- `canUploadSignImage` 仅在未提交或已驳回时为 `true`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxJobScheduleController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxJobScheduleServiceImpl.java`
