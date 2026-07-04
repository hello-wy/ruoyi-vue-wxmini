# /system/student/{id}

## 用途

- `GET /system/student/{id}`：查询学员管理详情。
- `PUT /system/student/{id}/situation`：保存后台维护的学员情况。
- `POST /system/student/{id}/binding`：绑定或改绑学员负责人；员工自认领也走该接口。

> 说明：这里的 `id` 为学员管理列表返回的 `user_info.id`。当前接口范围与 `GET /system/student/list` 保持一致，用于后台学员管理列表中的用户记录，不额外按 `userType` 二次过滤。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- `GET` 需要具备权限标识：`@ss.hasPermi('system:student:query')`。
- `PUT /situation` 需要具备权限标识：`@ss.hasPermi('system:student:edit')`。
- `POST /binding` 需要具备权限标识：`@ss.hasPermi('system:student:bind')`。

所有接口都会按当前后台用户的数据范围校验学员访问权限；无数据权限时返回非 200，消息为 `无权限访问学员数据` 或 `无权限绑定该学员`。

## 请求头

- `Authorization: Bearer <token>`
- `PUT` / `POST` 请求需要 `Content-Type: application/json`

## Path 参数

- `id`：学员管理列表返回的用户记录 ID，对应 `user_info.id`。

## GET 请求

### Query 参数

- 无。

### Body 示例

- 无。

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "userId": "wx-user-id",
    "displayName": "张三",
    "userName": "zhangsan",
    "phone": "13800000000",
    "realName": "张三",
    "nickName": "小张",
    "gender": 1,
    "age": 10,
    "userType": 1,
    "userTypeLabel": "学生",
    "companyName": null,
    "companyAddress": null,
    "companyPosition": null,
    "industry": null,
    "workYears": null,
    "personalIntro": null,
    "studentSituation": "学习主动性较好，数学基础需加强"
  }
}
```

## PUT /situation 请求

### Body 参数

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `studentSituation` | string | 否 | 学员情况。允许为空字符串，空字符串表示清空；最大 1000 字。 |

### Body 示例

```json
{
  "studentSituation": "学习主动性较好，数学基础需加强"
}
```

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "studentSituation": "学习主动性较好，数学基础需加强"
  }
}
```

## POST /binding 请求

绑定或改绑学员负责人。员工自认领时可以不传 `sysUserId`，服务端会强制绑定给当前登录员工。

### Body 参数

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `sysUserId` | number | 否 | 目标员工 `sys_user.user_id`；经理及以上绑定时必传，员工自认领可不传 |

### Body 示例

```json
{
  "sysUserId": 12
}
```

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "assignmentId": 1,
    "studentId": 8,
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
}
```

### 绑定规则

- 超级管理员和全国经理可绑定全部学员。
- 经理、市总、省总等非员工层级只能绑定自己 DataScope 范围内的学员给自己 DataScope 范围内的员工。
- 员工只能自认领自己部门及子部门范围内未绑定学员；不能替别人绑定，不能抢已绑定给别人的学员。
- 目标用户必须是启用、未删除、`adminLevel=employee` 且已配置所属部门的系统用户。

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 无权限时，请求会被拦截。
- 记录不存在时返回非 200，消息为 `学员不存在`。
- 无数据权限时返回非 200，消息为 `无权限访问学员数据` 或 `无权限绑定该学员`。
- 学员未设置归属部门且当前用户不是超级管理员/全国经理时，返回 `学员未设置归属部门，无法绑定员工`。
- 目标员工未配置所属部门时，返回 `绑定员工未配置所属部门，无法绑定学员`。
- `studentSituation` 会 trim 首尾空白后保存。

## 权限菜单配置

- `system:student:list`、`system:student:query`、`system:student:edit`、`system:student:bind` 需要在 `sys_menu` 中存在，才能分配给非超级管理员角色。
- 数据库脚本会补齐学员绑定按钮权限；具体角色授权仍按后台角色菜单配置执行。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/StudentController.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/StudentServiceImpl.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/StudentAccessServiceImpl.java`
