# POST /wxmini/tutoring/parents

## 用途

- `POST`：发布家教单（需登录）。

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
  "subject": "数学",
  "region": "鼓楼区",
  "methods": 1,
  "grade": "初一",
  "address": "南京市鼓楼区xx路",
  "salary": "200/次",
  "description": "希望一对一上门辅导"
}
```

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": 123456789012345678
}
```

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 未登录：`msg = 请先登录`。
- 用户不存在：`msg = 用户不存在`。
- 当前实现发布家教单时写入 `wechatUid`，`systemUid` 暂未在控制器中写入。
- 按 controller 业务语义该接口需要登录，但当前 `WxMiniJwtFilter.checkIsExcludeUri` 将 `/wxmini/tutoring/**` 整体排除在 JWT 解析之外；若不修正过滤器，`WxMiniUserContext` 不会被写入。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
