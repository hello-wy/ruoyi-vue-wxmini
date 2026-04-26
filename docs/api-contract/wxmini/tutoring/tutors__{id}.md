# GET /wxmini/tutoring/tutors/{id}

## 用途

- `GET`：教员详情（公开）。

## 鉴权

- 公开接口。

## 请求头

- 无。

## Path 参数

- `id`：路径参数。

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
    "realName": "张老师",
    "identity": 0,
    "subjects": "8,10",
    "areas": "320115,320114",
    "methods": 1,
    "status": 1,
    "experience": "3年家教经验\n擅长提分",
    "major": "数学与应用数学",
    "school": "南京大学",
    "degree": 1,
    "selfJudge": "认真负责，善于沟通",
    "certificateList": "教师资格证,英语六级",
    "certificates": "https://example.com/cert.jpg",
    "city": "江宁区"
  }
}
```

### 失败场景或特殊说明

- 以当前 controller/service 的实际校验结果为准。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
