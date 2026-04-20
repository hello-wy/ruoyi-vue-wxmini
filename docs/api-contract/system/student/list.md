# GET /system/student/list

## 用途
查询管理后台学员分页列表，支持按 `wx_user_profile.real_name` 搜索。

## 鉴权
- 需要 `Authorization: Bearer <token>`
- 需要具备权限标识：`system:student:list`

## 请求参数
- `pageNum`: 页码
- `pageSize`: 每页条数
- `realName`: 学员姓名关键字，匹配 `wx_user_profile.real_name`

## 成功响应示例
```json
{
  "code": 200,
  "msg": "查询成功",
  "rows": [
    {
      "id": 1,
      "userId": "uuid-1",
      "displayName": "张三",
      "realName": "张三",
      "userType": 1,
      "userTypeLabel": "学生"
    }
  ],
  "total": 1
}
```
