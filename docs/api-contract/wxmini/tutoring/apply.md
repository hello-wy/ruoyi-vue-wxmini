# POST /wxmini/tutoring/apply

## 用途

- `POST`：申请成为教员（isCertified 默认 0 待审核，需登录）。

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
  "realName": "张三",
  "school": "南京大学",
  "major": "数学",
  "subjects": "数学,物理",
  "areas": "鼓楼区",
  "methods": 1,
  "degree": 1,
  "certificate": "教师资格证",
  "certificates": "https://example.com/cert.jpg"
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
- 重复申请：`msg = 您已提交过申请，请勿重复提交`。
- 按 controller 业务语义该接口需要登录，但当前 `WxMiniJwtFilter.checkIsExcludeUri` 将 `/wxmini/tutoring/**` 整体排除在 JWT 解析之外；若不修正过滤器，`WxMiniUserContext` 不会被写入。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
