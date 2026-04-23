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
      "id": 1
    }
  ]
}
```

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxGrowupController.java`
