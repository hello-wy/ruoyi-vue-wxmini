# POST /system/record/export

## 用途

- `POST`：导出讲座签到记录列表。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:record:export')`。

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- 无。

## POST 请求

### Query 参数

- 其余筛选字段沿用 `SignInRecord` 对象。

### Body 示例

- 无。

### 成功响应示例

- 返回 Excel 文件流，不是 JSON 响应。

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 无权限时，请求会被拦截。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/SignInRecordController.java`
