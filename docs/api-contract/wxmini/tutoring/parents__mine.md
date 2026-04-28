# GET /wxmini/tutoring/parents/mine

## 用途

- `GET`：按传入用户 ID 查询该用户发布的家教单。

## 鉴权

- 当前实现未强制依赖 `Wx-Authorization`。

## 请求头

- 无强制要求。

## Path 参数

- 无。

## GET 请求

### Query 参数

- `id: string`，用户 ID，必填。

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

- 未传 `id`：`msg = 用户ID不能为空`。
- 用户不存在：`msg = 用户不存在`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
