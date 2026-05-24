# POST /system/tutoring-admin/orders

## 用途

- `POST`：后台为家长需求选择教员，创建或复用绑定关系，并生成家教待支付订单。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:parents:edit')`。

## 请求头

- `Authorization: Bearer <token>`

## Query 参数

- `parentId`：必填，家长需求 ID。
- `tutorId`：必填，教员 ID，使用 `tutors.id`。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 50001,
    "orderNo": "TUTOR202605191230001234",
    "bindingId": 20001,
    "parentId": 30001,
    "parentUserId": 101,
    "tutorId": 40001,
    "tutorUserId": 202,
    "serviceTimesSnapshot": "[{\"serviceDate\":\"2026-05-20\",\"startTime\":\"18:00\",\"endTime\":\"20:00\"}]",
    "lessonCount": 1,
    "hourlyPrice": 120.00,
    "totalAmount": 240.00,
    "commissionRate": 10.00,
    "status": 0
  }
}
```

## 失败场景或特殊说明

- `parentId` 或 `tutorId` 为空：`msg = 需求和教员不能为空`。
- 家长需求不存在：`msg = 家长需求不存在`。
- 教员不存在或未通过审核：`msg = 教员不存在或未通过审核`。
- 家长需求未关联平台用户：`msg = 家长需求未关联平台用户`。
- 教员用户不存在：`msg = 教员用户不存在`。
- 需求时薪预算为空或小于等于 0：`msg = 需求时薪预算无效`。
- 服务时段为空：`msg = 服务时段不能为空`。
- 服务时段结束时间不晚于开始时间：`msg = 服务时段不合法`。
- 若相同 `parentId + tutorId` 已存在未关闭绑定，会复用该绑定。
- 若该绑定下已有待支付订单，直接返回现有待支付订单，不重复插入业务订单。
- 若该绑定下已有已支付订单：`msg = 该绑定已完成下单`。
- 新订单状态为 `0`，表示待支付；家长需要在小程序端调用 `POST /wxmini/tutoring/orders/{orderNo}/pay` 拉起微信支付。
- 支付成功后复用家教支付回调闭环：订单置为已支付、绑定置为已下单、同一家长需求的其他绑定关闭，并生成家教课表。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/TutoringAdminController.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/TutoringSettlementServiceImpl.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/TutoringBindingServiceSupport.java`
