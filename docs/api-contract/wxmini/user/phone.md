# POST /wxmini/user/phone

## 用途

- `POST`：POST /wxmini/user/phone。

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

- 请求体为 `WxPhoneCodeRequest` JSON 对象，字段以对应 BO / domain 定义为准。

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxMaUserController.java`
