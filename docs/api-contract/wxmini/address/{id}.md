# DELETE /wxmini/address/{id}

## 用途

- `DELETE`：删除当前登录用户的服务地址。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

## Path 参数

- `id`：服务地址 ID。

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

- 未登录：`msg = 请先登录`。
- 地址不存在或不属于当前用户：`msg = 地址不存在或无权删除`。
- 若地址已被有效家教需求引用，service 层会拒绝删除：`msg = 该地址已关联家教需求，暂不能删除`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxAddressController.java`
