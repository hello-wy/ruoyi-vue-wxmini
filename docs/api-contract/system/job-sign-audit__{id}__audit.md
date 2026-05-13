# POST /system/job-sign-audit/{id}/audit

## 用途

- 审核指定签到记录的签到材料。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:record:edit')`。

## 请求头

- `Authorization: Bearer <token>`
- `Content-Type: application/json`

## Path 参数

- `id`：必填，签到记录 ID。

## Query 参数

- 无。

## Body 示例

```json
{
  "auditStatus": 2,
  "auditRemark": "材料清晰，审核通过"
}
```

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

## 失败场景或特殊说明

- `auditStatus` 仅允许传 `2`（通过）或 `3`（驳回）。
- `id` 为空时返回：`签到记录不能为空`。
- 审核状态非法时返回：`审核状态不合法`。
- 记录不存在或非兼职签到记录时返回：`签到记录不存在`。
- 尚未提交签到材料时返回：`该记录尚未提交签到材料`。
- 当前记录不处于待审核状态时返回：`该记录当前不可审核`。
- 驳回且未传 `auditRemark` 时，后端会自动写入：`签到材料审核未通过`。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/JobSignAuditController.java`
