# GET /wxmini/growup/enrollments/total

## 用途

获取当前登录用户的总学时余量。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": 20
}
```

## 失败说明

- 当前 token 对应不到用户时，返回 `code != 200` 且 `msg = 用户不存在`

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxGrowupController.java`
