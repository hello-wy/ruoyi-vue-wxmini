# POST|PUT /wxmini/tutoring/parents

## 用途

- `POST`：发布家教单（需登录）。
- `PUT`：修改当前登录用户发布的家教单（需登录）。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

## Path 参数

- `PUT` 时：`id` 为家教单 ID。
- `POST` 时：无。

## POST 请求

### Query 参数

- 无。

### Body 示例

```json
{
  "addressId": 1924135123456789012,
  "babyId": 1924135000000000001,
  "name": "周内晚间数学作业辅导",
  "phone": "13800138000",
  "grade": "初一",
  "subject": "数学",
  "methods": 1,
  "demandItems": "1,2,4",
  "genderRequirement": 0,
  "hourlyBudget": 80,
  "serviceDates": "2026-05-01,2026-05-03",
  "serviceTimes": "[{\"serviceDate\":\"2026-05-01\",\"startTime\":\"18:00\",\"endTime\":\"20:00\"},{\"serviceDate\":\"2026-05-03\",\"startTime\":\"20:30\",\"endTime\":\"22:30\"}]",
  "dayOfWeek": "5,7",
  "startTime": "18:00",
  "endTime": "20:00",
  "brief": "孩子目前基础一般，需要重点补作业和错题。",
  "requirements": "希望教员擅长沟通，能帮助孩子养成学习习惯。"
}
```

### 字段说明

- `genderRequirement`：陪伴官性别要求，`0=不限`、`1=男`、`2=女`
- `hourlyBudget`：陪伴官时薪预算，单位元/小时，需大于 `0`
- `serviceTimes`：建议传 JSON 字符串，当前每项至少包含 `serviceDate`、`startTime`、`endTime`

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": 1234567890123456789
}
```

### 失败场景或特殊说明

- 未登录：`msg = 请先登录`。
- 用户不存在：`msg = 用户不存在`。
- 非家长身份：`msg = 请先切换为家长身份`。
- 缺少或非法字段时，会按当前实现返回对应中文提示：
  - `请填写需求描述`
  - `请填写正确的手机号`
  - `请选择服务萌娃`
  - `请选择服务地址`
  - `请选择年级`
  - `请选择学科`
  - `请选择服务时段`
  - `请选择授课方式`
  - `请选择服务需求项目`
  - `请选择陪伴官性别要求`
  - `请填写时薪预算`
- 萌娃不属于当前用户：`msg = 萌娃信息不存在或无权使用`。
- 地址不属于当前用户：`msg = 服务地址不存在或无权使用`。
- `PUT` 时如果家教单不存在：`msg = 需求不存在`。
- `PUT` 时如果家教单不属于当前用户：`msg = 无权操作该需求`。
- 保存时会根据 `addressId` 回填家教单上的 `region`、`location`、`geo` 地址快照。
- `serviceTimes` 未显式传 `startTime/endTime` 时，后端会从 `serviceTimes` 中提取第一组时间回填到旧字段，兼容列表/详情展示。
- `dayOfWeek` 仍可继续传，用于兼容旧展示逻辑；当前前端由所选 `serviceDates` 推导生成。

## PUT 请求

### Query 参数

- 无。

### Body 示例

```json
{
  "addressId": 1924135123456789012,
  "babyId": 1924135000000000001,
  "name": "周末上门英语陪学",
  "phone": "13800138000",
  "grade": "五年级",
  "subject": "英语",
  "methods": 1,
  "demandItems": "2,3",
  "genderRequirement": 2,
  "hourlyBudget": 100,
  "serviceDates": "2026-05-10,2026-05-17",
  "serviceTimes": "[{\"serviceDate\":\"2026-05-10\",\"startTime\":\"09:00\",\"endTime\":\"11:00\"}]",
  "dayOfWeek": "7",
  "startTime": "09:00",
  "endTime": "11:00",
  "brief": "孩子愿意配合，需要帮助养成学习习惯。",
  "requirements": "最好有小学英语陪学经验。"
}
```

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 失败场景或特殊说明

- 同 `POST`，并额外校验当前用户是否拥有该家教单。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
