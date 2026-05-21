# GET /system/tutoring-admin/payroll/list

## 用途

- `GET`：后台分页查询家教结算单列表。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:wallet:withdrawRecords')`。

## 请求头

- `Authorization: Bearer <token>`

## Query 参数

- `pageNum` / `pageSize`：若依标准分页参数。
- `status`：可选，按结算状态筛选。
- `tutorUserId`：可选，按教员用户筛选。
- `orderNo`：可选，按家教订单号筛选。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 1,
  "rows": [
    {
      "id": 70001,
      "payrollNo": "TPAY-60001",
      "scheduleId": 60001,
      "orderNo": "TUTOR202605191230001234",
      "tutorUserId": 202,
      "grossAmount": 240.00,
      "commissionRate": 10.00,
      "commissionAmount": 24.00,
      "netAmount": 216.00,
      "status": 0,
      "walletBizId": "TUTORING_PAYROLL:60001",
      "tutorName": "王教员",
      "parentName": "周内晚间数学作业辅导"
    }
  ]
}
```

## 失败场景或特殊说明

- 状态约定：`0=待发放`、`1=已发放`。
- 数据来自独立表 `tutoring_payroll_item`，不会复用 `job_payroll_item`。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/TutoringAdminController.java`
- `ruoyi-system/src/main/resources/mapper/system/TutoringPayrollItemMapper.xml`
