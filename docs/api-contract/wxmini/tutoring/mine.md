# GET /wxmini/tutoring/mine

## 用途

- `GET`：查看当前登录用户的教员信息（需登录）。

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
  "data": {
    "id": 1
  }
}
```

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 未登录：`msg = 请先登录`。
- 按 controller 业务语义该接口需要登录，但当前 `WxMiniJwtFilter.checkIsExcludeUri` 将 `/wxmini/tutoring/**` 整体排除在 JWT 解析之外；若不修正过滤器，`WxMiniUserContext` 不会被写入。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
