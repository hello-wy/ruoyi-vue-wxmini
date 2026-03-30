# GET /wxmini/tutoring/mine

## 用途

查看当前登录用户自己的教员资料。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "uid": 1
  }
}
```

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
