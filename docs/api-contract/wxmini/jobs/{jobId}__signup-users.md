# GET /wxmini/jobs/{jobId}/signup-users

## 用途

获取指定岗位已支付报名用户池，供商家查询报名人员、发工资、查看签到图片审核状态时使用。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`
- 仅岗位发布商家可访问（后端校验 `UserInfo.userType = 2` 且 `UserInfo.id = daily_jobs.publisher_uid`）

## Path 参数

| 参数   | 类型   | 必填 | 说明       |
|--------|--------|------|------------|
| jobId  | Long   | 是   | 兼职岗位 ID |

## Query 参数

| 参数    | 类型   | 必填 | 说明                             |
|---------|--------|------|----------------------------------|
| keyword | String | 否   | 按姓名 / 昵称 / 手机号模糊搜索 |

## 成功响应

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
      "attendanceStatus": 1,
      "attendanceStatusLabel": "已签到",
      "auditStatus": 2,
      "auditStatusLabel": "已通过",
      "auditRemark": null,
      "signTime": "2026-05-13 09:20:00",
      "signedCount": 1,
      "signImageUrl": "/profile/job-sign/321/1715832000000.jpg",
      "submitTime": "2026-05-13 09:21:00",
      "payrollPaid": true,
      "payrollItemStatus": 1
    }
  ]
}
```

## 字段说明

| 字段                    | 类型    | 说明                                                                 |
|-------------------------|---------|----------------------------------------------------------------------|
| userInfoId              | Long    | 报名用户的 `user_info.id`；缺失 user_info 时兜底为 `0`              |
| displayName             | String  | 显示名（优先 real_name，其次 nick_name）；缺失时兜底为 `"未注册用户"` |
| phoneMasked             | String  | 脱敏手机号，如 `138****8000`；缺失时兜底为空串                       |
| orderNo                 | String  | 报名订单号                                                           |
| attendanceStatus        | Integer | 签到状态码：`0` 未签到、`1` 已签到、`2` 迟到、`3` 已取消            |
| attendanceStatusLabel   | String  | 签到状态中文标签                                                     |
| auditStatus             | Integer | 签到图片审核状态：`0` 未提交、`1` 待审核、`2` 已通过、`3` 已驳回    |
| auditStatusLabel        | String  | 审核状态中文标签                                                     |
| auditRemark             | String  | 审核备注，可为 null                                                  |
| signTime                | String  | 签到时间，格式 `yyyy-MM-dd HH:mm:ss`；未签到时为 null               |
| signedCount             | Integer | 该用户当前岗位签到记录数                                             |
| signImageUrl            | String  | 签到图片相对路径；未上传时为 null                                    |
| submitTime              | String  | 签到图片提交时间，格式 `yyyy-MM-dd HH:mm:ss`；未提交时为 null       |
| payrollPaid             | Boolean | 该用户在当前岗位下是否已有工资入账记录；`true` 表示已支付            |
| payrollItemStatus       | Integer | 工资条状态：`1` 已发工资、`0` 未发工资                               |

## 下架后访问行为

当岗位状态 `daily_jobs.status = 3`（已下架/已取消）后：

- 该接口**仍可被发布商家正常访问**，返回完整的报名用户列表，商家可继续完成审核与发工资操作。
- 下架岗位仅在 `GET /wxmini/jobs/mine/published`（`getMyPublishedJobs`）列表中被后端过滤隐藏，不再出现在商家的岗位列表页面。
- 商家可通过 `pages/jobs/signup-users?jobId={jobId}` URL 直达已下架岗位的报名人员面板。

## 失败场景

| 场景                         | 响应                                         |
|------------------------------|----------------------------------------------|
| 未登录 / token 无效          | `{ "code": 401, "msg": "请先登录" }`        |
| 非商家身份（userType ≠ 2）   | `{ "code": 500, "msg": "仅商家可查看报名用户" }` |
| 非岗位发布商家               | `{ "code": 500, "msg": "仅岗位发布商家可查看报名用户" }` |
| 岗位不存在                   | `{ "code": 500, "msg": "岗位不存在" }`      |

## 实现来源文件

- Controller: `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxJobScheduleController.java`
- Service: `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxJobScheduleServiceImpl.java`
- VO: `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/vo/WxSignupUserVo.java`
- Mapper: `ruoyi-system/src/main/resources/mapper/wxmini/JobSignupOrderMapper.xml`
