# GET /system/enrollment/list

## 用途

- `GET`：查询学籍信息分页列表，返回学籍数量以及学员、课程展示字段。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:enrollment:list')`。

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- 无。

## GET 请求

### Query 参数

- `pageNum`：若依标准分页页码。
- `pageSize`：若依标准分页每页条数。
- `uid`：学员 ID，对应 `user_info.id`。
- `lectureId`：课程 ID，对应 `lectures.id`。
- `studentName`：学员姓名/昵称/用户名，模糊匹配。
- `phone`：学员手机号，模糊匹配。
- `lectureName`：课程名称，模糊匹配。
- `total`：总学籍数，精确匹配。
- `remain`：剩余学籍数，精确匹配。

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
      "id": 1898700000000030,
      "uid": 1,
      "studentName": "张三",
      "phone": "13800000000",
      "lectureId": 100,
      "lectureName": "经营智慧",
      "lectureTime": "2026-07-02 09:00:00",
      "location": "南京市",
      "total": 40,
      "remain": 39,
      "sharedCount": 1,
      "createTime": "2026-07-02 10:00:00",
      "updateTime": "2026-07-02 10:30:00",
      "isDeleted": 0
    }
  ]
}
```

### 字段说明

- `uid`：学员 ID，对应 `user_info.id`。
- `studentName`：优先取 `wx_user_profile.real_name`，其次 `wx_user_profile.nick_name`，最后 `user_info.user_name`。
- `lectureName`：课程名称，对应 `lectures.name`。
- `sharedCount`：该学员该课程累计分享给其他学员的数量。
- 列表默认只返回 `is_deleted = 0` 的正常学籍。

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 无权限时，请求会被拦截。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/StudentEnrollmentController.java`
- `ruoyi-system/src/main/resources/mapper/system/StudentEnrollmentMapper.xml`
