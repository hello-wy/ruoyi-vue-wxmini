# DELETE /system/tradeOrder/{ids}

## 用途

- `DELETE`：删除交易订单。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:tradeOrder:remove')`。

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- `ids`：路径参数。

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
- 无权限时，请求会被拦截。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/TradeOrderController.java`
