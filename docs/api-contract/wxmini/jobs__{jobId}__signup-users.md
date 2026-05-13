# GET /wxmini/jobs/{jobId}/signup-users

## 用途

- 查询当前登录商家名下某个兼职岗位的已报名用户池，并返回签到材料与审核状态。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

## Path 参数

- `jobId`：必填，兼职岗位 ID。

## Query 参数

- `keyword`：可选，按用户名称或手机号模糊筛选，实际以 service 实现为准。

## Body 示例

- 无。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "userInfoId": 101,
      "displayName": "张三",
      "phoneMasked": "138****0000",
      "orderNo": "JOB202605130001",
      "attendanceStatus": 1,
      "attendanceStatusLabel": "已签到",
      "signTime": "2026-05-13 09:20:00",
      "signedCount": 1,
      "signImageUrl": "/profile/sign/job/12/20260513_xxx.png",
      "auditStatus": 2,
      "auditStatusLabel": "已通过",
      "auditRemark": null,
      "submitTime": "2026-05-13 09:21:00"
    }
  ]
}
```

## 失败场景或特殊说明

- 未登录时返回：`请先登录`。
- 该接口返回对象结果，不走若依分页包装。
- `auditStatus` 约定：`0=未提交`、`1=待审核`、`2=已通过`、`3=已驳回`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxJobScheduleController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxJobScheduleServiceImpl.java`
