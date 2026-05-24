# GET|DELETE /wxmini/tutoring/parents/{id}

## 用途

- `GET`：查询家教单详情（公开）。
- `DELETE`：删除当前登录用户发布的家教单。

## 鉴权

- `GET`：公开接口。
- `DELETE`：需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `GET`：无。
- `DELETE`：`Wx-Authorization: Bearer <token>`。

## Path 参数

- `id`：家教单 ID。

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
    "genderRequirement": 0,
    "hourlyBudget": 80.00,
    "serviceDates": "2026-05-01,2026-05-03",
    "serviceTimes": "[{\"serviceDate\":\"2026-05-01\",\"startTime\":\"18:00\",\"endTime\":\"20:00\"}]",
    "dayOfWeek": "5,7",
    "startTime": "18:00",
    "endTime": "20:00",
    "region": "江苏省 南京市 鼓楼区",
    "location": "鼓楼区xx小区 3栋 1201",
    "geo": "118.7901,32.0601",
    "brief": "孩子目前基础一般，需要重点补作业和错题。",
    "requirements": "希望教员擅长沟通，能帮助孩子养成学习习惯。",
    "status": 0,
    "createDate": "2026-05-01",
    "updateDate": "2026-05-01"
  }
}
```

### 字段说明

- `genderRequirement`：陪伴官性别要求，`0=不限`、`1=男`、`2=女`
- `hourlyBudget`：陪伴官时薪预算，单位元/小时

### 失败场景或特殊说明

- 当前实现未对详情不存在场景单独包装错误，若记录不存在，`data` 可能为 `null`。

## DELETE 请求

### Query 参数

- 无。

### Body 示例

- 无。

### 成功响应示例

```json
{
  "code": 200,
  "msg": "删除成功"
}
```

### 失败场景或特殊说明

- 未登录：`msg = 请先登录`。
- 用户不存在：`msg = 用户不存在`。
- 家教单不存在：`msg = 需求不存在`。
- 非本人家教单：`msg = 无权操作该需求`。
- 删除失败：`msg = 删除失败`。
- 删除需求前会关闭该需求关联的家教绑定，避免旧绑定继续出现在可下单列表。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
