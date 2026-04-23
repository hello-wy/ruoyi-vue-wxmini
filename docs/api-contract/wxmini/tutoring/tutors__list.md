# GET /wxmini/tutoring/tutors/list

## 用途

- `GET`：GET /wxmini/tutoring/tutors/list。

## 鉴权

- 公开接口。

## 请求头

- 无。

## Path 参数

- 无。

## GET 请求

### Query 参数

- `pageNum`：若依标准分页页码。
- `pageSize`：若依标准分页每页条数。
- `1`：`long`，默认 `1`。
- `5`：`long`，默认 `5`。
- `subject`：`String`。
- `region`：`String`。
- `methods`：`Long`。
- `grade`：`Long`。

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

- 以当前 controller/service 的实际校验结果为准。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
