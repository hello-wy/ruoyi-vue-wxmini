# GET /system/record/myRecords

## 用途

- `GET`：查询当前登录用户的个人签到记录（含讲座信息）。

## 鉴权

- 需要 `Authorization: Bearer <token>`。

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- 无。

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
    "id": 1
  }
}
```

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/SignInRecordController.java`
