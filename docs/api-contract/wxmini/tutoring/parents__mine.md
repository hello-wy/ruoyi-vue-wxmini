# GET /wxmini/tutoring/parents/mine

## 用途

查看当前登录用户发布的家教单列表。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "id": 1
    }
  ]
}
```

## 失败说明

- 未登录：`msg = 请先登录`
- 用户不存在：`msg = 用户不存在`

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
