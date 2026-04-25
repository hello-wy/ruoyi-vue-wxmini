# GET|POST /wxmini/portal/{appid}

## 用途

- `GET`：GET /wxmini/portal/{appid}。
- `POST`：接收微信服务器推送消息/事件（由微信服务器发起）。

## 鉴权

- 公开接口。

## 请求头

- 无。

## Path 参数

- `appid`：路径参数。

## GET 请求

### Query 参数

- `signature`：`String`。
- `timestamp`：`String`。
- `nonce`：`String`。
- `echostr`：`String`。

### Body 示例

- 无。

### 成功响应示例

```text
echostr
```

### 失败场景或特殊说明

- GET 鉴权失败时返回 `非法请求` 或直接抛出参数异常。
- POST 遇到未知 `encrypt_type` 时会抛出 `不可识别的加密类型`。

## POST 请求

### Query 参数

- `msg_signature`：`String`。
- `encrypt_type`：`String`。
- `signature`：`String`。
- `timestamp`：`String`。
- `nonce`：`String`。

### Body 示例

- 微信服务器推送的原始消息体，可能是 JSON 或 XML。

### 成功响应示例

```text
success
```

### 失败场景或特殊说明

- GET 鉴权失败时返回 `非法请求` 或直接抛出参数异常。
- POST 遇到未知 `encrypt_type` 时会抛出 `不可识别的加密类型`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxPortalController.java`
