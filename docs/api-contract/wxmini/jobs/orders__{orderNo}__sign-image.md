# POST /wxmini/jobs/orders/{orderNo}/sign-image

## 用途

- `POST`：当前登录兼职用户为自己的已支付报名订单提交签到图片。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。
- 仅订单所属小程序用户可提交。

## Path 参数

- `orderNo`：报名订单号。

## 请求 Body

```json
{
  "signImageUrl": "/profile/job-sign/321/1715832000000.jpg"
}
```

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "orderNo": "JOB202605020001",
    "jobId": 1001,
    "title": "活动协助兼职",
    "userInfoId": 2001,
    "displayName": "张三",
    "phoneMasked": "138****8000",
    "attendanceStatus": 0,
    "attendanceStatusLabel": "未签到",
    "signImageUrl": "/profile/job-sign/321/1715832000000.jpg",
    "auditStatus": 1,
    "auditStatusLabel": "待审核",
    "auditRemark": null,
    "submitTime": "2026-05-17 10:30:00",
    "auditTime": null,
    "signTime": null
  }
}
```

## 字段说明

- `signImageUrl`：由 `POST /wxmini/common/uploadJobSignImage` 返回的 `fileName`。
- `auditStatus`：提交后置为 `1` 待审核。
- `auditStatusLabel`：审核状态文案，`0` 未提交，`1` 待审核，`2` 已通过，`3` 已驳回。
- `auditRemark` / `auditTime`：重新提交图片时清空当前订单历史审核备注与审核时间。
- 兼职签到记录按 `orderNo` 对应的报名订单隔离保存；同一岗位重新报名后，新的签到图不会覆盖历史订单记录。

## 失败场景或特殊说明

- 未登录返回业务错误：`请先登录`。
- `signImageUrl` 为空返回业务错误：`签到图片不能为空`。
- 订单不存在返回业务错误：`订单不存在`。
- 提交非本人订单返回业务错误：`只能提交自己的兼职签到图片`。
- 非已支付订单返回业务错误：`订单未支付`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxJobScheduleController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxJobScheduleServiceImpl.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/JobAttendanceAdminServiceImpl.java`
