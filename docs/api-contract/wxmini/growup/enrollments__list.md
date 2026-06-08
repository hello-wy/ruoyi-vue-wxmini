# GET /wxmini/growup/enrollments/list

## 用途

- `GET`：获取当前登录用户的个人学籍列表（需登录）。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

## Path 参数

- 无。

## GET 请求

### Query 参数

- `pageNum`：若依标准分页页码。
- `pageSize`：若依标准分页每页条数。

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
      "id": 20,
      "lectureId": 99,
      "lectureName": "幸福解码",
      "total": 5,
      "remain": 3
    }
  ]
}
```

### 响应字段补充说明

- `id`：学籍记录 ID。
- `lectureId`：该学籍绑定的课程/讲座 ID。
- `lectureName`：课程/讲座名称。
- `total`：该课程总学籍数。
- `remain`：该课程剩余可用学籍数。

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxGrowupController.java`
