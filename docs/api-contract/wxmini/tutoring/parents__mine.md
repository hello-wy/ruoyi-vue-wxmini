# GET /wxmini/tutoring/parents/mine

## 用途

- `GET`：查询当前登录家长用户自己发布的家教单列表。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

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
  "data": [
    {
      "id": 1234567890123456789,
      "wechatUid": "wx-user-123",
      "systemUid": 101,
      "babyId": 1924135000000000001,
      "addressId": 1924135123456789012,
      "name": "周内晚间数学作业辅导",
      "phone": "13800138000",
      "grade": "初一",
      "subject": "数学",
      "methods": 1,
      "demandItems": "1,2,4",
      "serviceDates": "2026-05-01,2026-05-03",
      "serviceTimes": "[{\"startTime\":\"18:00\",\"endTime\":\"20:00\"}]",
      "dayOfWeek": "5,7",
      "startTime": "18:00",
      "endTime": "20:00",
      "region": "江苏省 南京市 鼓楼区",
      "location": "鼓楼区xx小区 3栋 1201",
      "geo": "118.7901,32.0601",
      "brief": "孩子目前基础一般，需要重点补作业和错题。",
      "requirements": "女教员优先，沟通耐心。",
      "status": 0,
      "createDate": "2026-05-01",
      "updateDate": "2026-05-01"
    }
  ]
}
```

### 失败场景或特殊说明

- 未登录：`msg = 请先登录`。
- 用户不存在：`msg = 用户不存在`。
- 当前按 `wechatUid` 查询当前用户发布的家教单。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
