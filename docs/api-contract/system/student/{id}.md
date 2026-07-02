# /system/student/{id}

## 用途

- `GET /system/student/{id}`：查询学员管理详情。
- `PUT /system/student/{id}/situation`：保存后台维护的学员情况。

> 说明：这里的 `id` 为学员管理列表返回的 `user_info.id`。当前接口范围与 `GET /system/student/list` 保持一致，用于后台学员管理列表中的用户记录，不额外按 `userType` 二次过滤。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- `GET` 需要具备权限标识：`@ss.hasPermi('system:student:query')`。
- `PUT` 需要具备权限标识：`@ss.hasPermi('system:student:edit')`。

## 请求头

- `Authorization: Bearer <token>`
- `PUT` 请求需要 `Content-Type: application/json`

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

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 无权限时，请求会被拦截。
- 记录不存在时返回非 200，消息为 `学员不存在`。
- `studentSituation` 会 trim 首尾空白后保存。

## 权限菜单配置

- `system:student:list`、`system:student:query`、`system:student:edit` 需要在 `sys_menu` 中存在，才能分配给非超级管理员角色。
- 本次数据库脚本会补齐学员管理菜单和查询/修改按钮权限；具体角色授权仍按后台角色菜单配置执行。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/StudentController.java`
