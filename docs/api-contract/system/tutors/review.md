# PUT /system/tutors/review

## 用途

- `PUT`：审核教员认证状态（0-待审核 1-已通过 2-已拒绝）。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:tutors:review')`。

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- 无。

## PUT 请求

### Query 参数

- 无。

### Body 示例

```json
{
  "id": 1,
  "isCertified": 1
}
```

### 成功响应示例

```json
{
  "code": 200,
  "msg": "审核操作成功"
}
```

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 无权限时，请求会被拦截。
- 参数缺失：`msg = 参数不能为空`。
- 状态非法：`msg = isCertified 参数非法，只允许 0/1/2`。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/TutorsController.java`
