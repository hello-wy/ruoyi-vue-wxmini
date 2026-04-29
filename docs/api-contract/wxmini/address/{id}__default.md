# PUT /wxmini/address/{id}/default

## 用途

- `PUT`：将指定服务地址设为当前登录用户的默认地址。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

## Path 参数

- `id`：服务地址 ID。

## PUT 请求

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
- 地址不存在或不属于当前用户：`msg = 地址不存在或无权操作`。
- service 层会先清空当前用户其他默认地址，再设置当前地址为默认地址。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxAddressController.java`
