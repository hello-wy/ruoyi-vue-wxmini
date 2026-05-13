# GET /system/job-sign-audit/list

## 用途

- 查询指定兼职岗位下已支付报名用户的签到审核列表。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:record:list')`。

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- 无。

## Query 参数

- `jobId`：必填，兼职岗位 ID。
- `pageNum`：若依标准分页页码。
- `pageSize`：若依标准分页每页条数。

## Body 示例

- 无。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 1,
  "rows": [
    {
      "id": 88,
      "recordId": 88,
      "jobId": 12,
      "userInfoId": 101,
      "userId": "wx-user-1",
      "displayName": "张三",
      "userName": "张三",
      "phoneMasked": "138****0000",
      "orderNo": "JOB202605130001",
      "attendanceStatus": 1,
      "attendanceStatusLabel": "已签到",
      "signImageUrl": "/profile/sign/job/12/20260513_xxx.png",
      "auditStatus": 1,
      "auditStatusLabel": "待审核",
      "auditRemark": null,
      "signTime": "2026-05-13 09:20:00",
      "submitTime": "2026-05-13 09:21:00",
      "auditTime": null
    }
  ]
}
```

## 失败场景或特殊说明

- 无权限时请求会被拦截。
- `auditStatus` 约定：`1=待审核`、`2=已通过`、`3=已驳回`。
- 列表只返回和该兼职岗位相关的签到审核记录。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/JobSignAuditController.java`
