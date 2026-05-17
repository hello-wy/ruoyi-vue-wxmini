# GET /wxmini/jobs/{jobId}/signup-users

## 用途

- `GET`：获取指定岗位已支付报名用户池，供商家查询报名人员、发工资、查看签到图片审核状态时使用。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。
- 仅岗位发布商家可访问。

## Query 参数

- `keyword`：可选，按姓名 / 昵称 / 手机号搜索。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "userInfoId": 2001,
      "displayName": "张三",
      "phoneMasked": "138****8000",
      "orderNo": "JOB202605020001",
      "attendanceStatus": 0,
      "attendanceStatusLabel": "未签到",
      "signImageUrl": "/profile/job-sign/321/1715832000000.jpg",
      "auditStatus": 1,
      "auditStatusLabel": "待审核",
      "auditRemark": null,
      "signTime": null,
      "signedCount": 0,
      "submitTime": "2026-05-17 10:00:00",
      "payrollPaid": true,
      "payrollItemStatus": 1
    }
  ]
}
```

## 字段说明

- `orderNo`：报名订单号。
- `attendanceStatus` / `attendanceStatusLabel`：后台签到状态。
- `signImageUrl`：报名用户已上传的签到图片相对路径；未上传时为空。
- `auditStatus` / `auditStatusLabel`：签到图片审核状态，`0` 未提交，`1` 待审核，`2` 已通过，`3` 已驳回。
- `auditRemark`：审核备注。
- `signTime`：签到通过或记录签到的时间。
- `signedCount`：该用户当前岗位签到记录数。
- `submitTime`：签到图片提交时间。
- `payrollPaid = true` 表示该用户在当前岗位下已有工资入账记录，前端应展示为“已支付/不可重复加入批次”。
- `payrollItemStatus = 1` 表示已发工资，`0` 表示未发工资。

## 失败场景或特殊说明

- 非商家身份访问会返回业务错误。
- 非岗位发布商家访问会返回业务错误。
- 仅返回该岗位已支付报名用户。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxJobScheduleController.java`
