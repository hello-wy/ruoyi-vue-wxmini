# GET|DELETE /wxmini/baby/{id}

## 用途

- `GET`：查询当前登录用户萌娃详情。
- `DELETE`：删除萌娃。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

## Path 参数

- `id`：路径参数。

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
    "id": 1
  }
}
```

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 记录不存在时：`msg = 萌娃信息不存在`。

## DELETE 请求

### Query 参数

- 无。

### Body 示例

- 无。

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 记录不存在时：`msg = 萌娃信息不存在`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxBabyController.java`
