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
      "id": 1,
      "identity": 0,
      "status": 1,
      "realName": "张老师",
      "school": "南京大学",
      "major": "数学与应用数学",
      "subjects": "8,10",
      "areas": "320115,320114",
      "methods": 1,
      "degree": 1,
      "city": "江宁区",
      "certificateList": "教师资格证,英语六级",
      "certificates": "https://example.com/cert.jpg"
    }
  ]
}
```

### 失败场景或特殊说明

- 以当前 controller/service 的实际校验结果为准。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
