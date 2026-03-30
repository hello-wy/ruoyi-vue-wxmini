# POST /wxmini/tutoring/apply

## 用途

当前登录用户申请成为教员。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`

## Body

- 请求体为 `Tutors` 对象，字段以实际业务表结构为准

## 成功响应示例

```json
{
  "code": 200,
  "msg": "申请成功，请等待审核"
}
```

## 失败说明

- 未登录：`msg = 请先登录`
- 重复申请：`msg = 您已提交过申请，请勿重复提交`

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
