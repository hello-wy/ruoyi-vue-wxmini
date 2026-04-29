# GET /wxmini/tutoring/parents/mine

## 用途

- `GET`：查询当前登录家长用户自己发布的家教单。

## 鉴权

- 必须携带 `Wx-Authorization`。

## 请求头

- `Wx-Authorization: Bearer <token>`

## Path 参数

- 无。

## GET 请求

### Query 参数

- 无。

### Body 示例

- 无。

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "id": 1,
      "wechatUid": "user-123"
    }
  ]
}
```

### 失败场景或特殊说明

- 未登录：`msg = 请先登录`。
- 用户不存在：`msg = 用户不存在`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
