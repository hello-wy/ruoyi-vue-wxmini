# GET /system/questionnaire/getQuestionnaire/{lecturesId}

## 用途

- `GET`：根据课程ID查询关联的问卷列表。

## 鉴权

- 需要 `Authorization: Bearer <token>`。

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- `lecturesId`：路径参数。

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

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/QuestionnaireController.java`
