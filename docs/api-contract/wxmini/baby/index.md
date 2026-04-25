# POST|PUT /wxmini/baby

## 用途

- `POST`：新增萌娃。
- `PUT`：修改萌娃。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

## Path 参数

- 无。

## POST 请求

### Query 参数

- 无。

### Body 示例

```json
{
  "id": 1,
  "realName": "张小明",
  "nickName": "明明",
  "birthDate": "2020-09-01",
  "gender": 1,
  "schoolName": "鼓楼实验幼儿园",
  "grade": "中班",
  "specialNote": "对花生过敏"
}
```

### 成功响应示例

```json
{
  "code": 200,
  "msg": "新增成功",
  "data": 123456789012345678
}
```

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 未登录：`msg = 请先登录`。
- 参数校验失败时返回对应中文提示。

## PUT 请求

### Query 参数

- 无。

### Body 示例

```json
{
  "id": 1,
  "realName": "张小明",
  "nickName": "明明",
  "birthDate": "2020-09-01",
  "gender": 1,
  "schoolName": "鼓楼实验幼儿园",
  "grade": "中班",
  "specialNote": "对花生过敏"
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

- 未登录或 token 无效时，请求会失败。
- 未登录：`msg = 请先登录`。
- 参数校验失败时返回对应中文提示。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxBabyController.java`
