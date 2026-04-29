# GET /wxmini/tutoring/mine

## 用途

- `GET`：查看当前登录用户的教员信息（需登录）。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

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
  "data": {
    "id": 1,
    "uid": "wx-user-123",
    "status": 1
  }
}
```

### 失败场景或特殊说明

- 未登录：`msg = 请先登录`。
- 当前用户尚未提交教员申请时，`data` 可能为 `null`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
