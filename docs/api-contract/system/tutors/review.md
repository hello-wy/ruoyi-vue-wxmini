# PUT /system/tutors/review

## 用途

管理员审核教员认证状态。

## 鉴权

- 需要 `Authorization: Bearer <token>`
- 需要具备权限标识：`system:tutors:edit`

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- 无

## Body 示例

```json
{
  "id": 10001,
  "isCertified": 1
}
```

## 请求字段

- `id`：必填，教员 ID
- `isCertified`：必填，仅允许 `0`、`1`、`2`

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

## 失败说明

- 参数为空：`msg = 参数不能为空`
- 参数非法：`msg = isCertified 参数非法，只允许 0/1/2`
- 记录不存在或更新失败：`msg = 操作失败`

## 特殊说明

- 该接口只更新 `id` 和 `isCertified`，不会改动其他教员字段。
- 该接口是后台管理侧审核入口，不是 `/wxmini/**` 小程序接口。

## 实现来源

- `ruoyi-vue-wxmini/ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/TutorsController.java`
