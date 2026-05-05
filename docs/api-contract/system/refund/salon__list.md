# GET /system/refund/salon/list

## 用途

- 查询沙龙活动退款管理订单列表。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:refund:list')`。

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- 无。

## Query 参数

- `salonId`：选填，沙龙活动 ID。
- `pageNum`：若依标准分页页码。
- `pageSize`：若依标准分页每页条数。

## Body 示例

- 无。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 1,
  "rows": [
    {
      "id": 1,
      "orderNo": "SALON202605030001",
      "userId": "wx-user-1",
      "salonId": 1,
      "title": "组局思维",
      "amount": 128.00,
      "status": "PAID",
      "payTime": "2026-05-03 10:00:00",
      "refundTime": null,
      "createTime": "2026-05-03 10:00:00",
      "updateTime": "2026-05-03 10:00:00"
    }
  ]
}
```

## 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 无权限时，请求会被拦截。
- 当前列表只返回 `PAID` 和 `REFUNDED` 状态的沙龙订单。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/RefundController.java`
