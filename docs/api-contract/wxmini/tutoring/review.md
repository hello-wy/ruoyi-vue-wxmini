# PUT /wxmini/tutoring/review

## 用途

管理员审核教员认证状态。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`

## Query 参数

- `id`：必填，教员 ID
- `isCertified`：必填，仅允许 `0`、`1`、`2`

## 成功响应示例

```json
{
  "code": 200,
  "msg": "审核操作成功"
}
```

## 失败说明

- 无权限：`msg = 无权限操作`
- 参数非法：`msg = isCertified 参数非法，只允许 0/1/2`

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
