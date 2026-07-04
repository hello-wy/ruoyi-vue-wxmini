# GET /system/student/list

## 用途

- `GET`：查询学员分页列表。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:student:list')`。

## 数据范围

后台会根据当前登录系统用户强制过滤学员范围，前端传参不能扩大权限范围。

- 超级管理员：查看全部学员。
- `adminLevel = national_general_manager`：查看全部学员。
- `adminLevel = employee`：查看已绑定给自己的学员；可查看自己部门及子部门范围内未绑定学员，用于自认领。
- 经理、市总、省总等非员工层级：按若依角色 DataScope + 部门树查看可见员工名下已绑定学员，以及可见部门内未绑定学员。
- 历史学员如果没有 `student_staff_assignment` 归属记录：除超级管理员和全国经理外，不会出现在列表；需要先通过数据脚本补齐归属部门。

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- 无。

## GET 请求

### Query 参数

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageNum` | number | 否 | 若依标准分页页码 |
| `pageSize` | number | 否 | 若依标准分页每页条数 |
| `realName` | string | 否 | 学员姓名 / 昵称 / 用户名，模糊匹配 |
| `phone` | string | 否 | 学员手机号，模糊匹配 |
| `assignmentStatus` | string | 否 | `bound` 已绑定、`unbound` 未绑定；其他值按全部处理 |
| `boundUserId` | number | 否 | 按绑定员工过滤；仍会叠加当前登录用户的数据范围 |

### Body 示例

- 无。

### 成功响应示例

```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 1,
  "rows": [
    {
      "id": 1,
      "userId": "uuid-1",
      "displayName": "张三",
      "realName": "张三",
      "phone": "13800000000",
      "userType": 1,
      "userTypeLabel": "学生",
      "assignmentId": 10,
      "ownerDeptId": 101,
      "ownerDeptName": "杭州校区",
      "boundUserId": 12,
      "boundUserName": "zhangsan",
      "boundUserNickName": "张三",
      "boundUserPhone": "13800000000",
      "boundUserAdminLevel": "employee",
      "boundDeptName": "杭州校区",
      "bound": true,
      "canBind": true,
      "canClaim": false
    }
  ]
}
```

### rows 字段说明

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `id` | number | 学员 ID，对应 `user_info.id` |
| `userId` | string | 小程序用户标识 |
| `displayName` | string | 展示名称 |
| `realName` | string | 实名 |
| `phone` | string | 手机号 |
| `userType` / `userTypeLabel` | number/string | 用户类型与展示文案 |
| `assignmentId` | number | 学员归属/绑定记录 ID |
| `ownerDeptId` / `ownerDeptName` | number/string | 学员归属部门 |
| `boundUserId` | number | 当前绑定员工用户 ID，未绑定为空 |
| `boundUserName` | string | 绑定员工账号 |
| `boundUserNickName` | string | 绑定员工昵称 |
| `boundUserPhone` | string | 绑定员工手机号 |
| `boundUserAdminLevel` | string | 绑定员工层级 |
| `boundDeptName` | string | 绑定员工部门 |
| `bound` | boolean | 是否已绑定员工 |
| `canBind` | boolean | 当前登录用户是否可绑定/改绑该学员 |
| `canClaim` | boolean | 当前登录员工是否可自认领该学员 |

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 无权限时，请求会被拦截。
- 分页 `total` 按 SQL 层数据范围过滤后的结果统计。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/StudentController.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/StudentServiceImpl.java`
- `ruoyi-system/src/main/resources/mapper/wxmini/WxUserProfileMapper.xml`
