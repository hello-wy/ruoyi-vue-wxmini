# GET /system/refund/job/list

## 用途

- 查询指定兼职岗位的退款管理订单列表。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:refund:list')`。

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- 无。

## Query 参数

- `jobId`：必填，兼职岗位 ID。
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
      "orderId": 1,
      "orderNo": "JOB202605030001",
      "userId": "wx-user-1",
      "jobId": 1,
      "jobTitle": "周末活动协助",
      "userName": "张三",
      "amount": 30.00,
      "status": 1,
      "signedIn": true,
      "payTime": "2026-05-03 10:00:00",
      "refundTime": null
    }
  ]
}
```

## 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 无权限时，请求会被拦截。
- `jobId` 缺失时，请求参数校验失败。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/RefundController.java`
