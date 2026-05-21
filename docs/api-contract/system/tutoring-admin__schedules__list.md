# GET /system/tutoring-admin/schedules/list

## 用途

- `GET`：后台分页查询家教课表审核列表。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:record:list')`。

## 请求头

- `Authorization: Bearer <token>`

## Query 参数

- `pageNum` / `pageSize`：若依标准分页参数。
- `status`：可选，按课表状态筛选。
- `parentUserId`：可选，按家长用户筛选。
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
      "id": 60001,
      "orderNo": "TUTOR202605191230001234",
      "status": 1,
      "serviceDate": "2026-05-20",
      "startTime": "18:00",
      "endTime": "20:00",
      "grossAmount": 240.00,
      "netAmount": 216.00,
      "parentName": "周内晚间数学作业辅导",
      "tutorName": "王教员"
    }
  ]
}
```

## 失败场景或特殊说明

- 当前实现复用 `TutoringSchedule` 查询对象做筛选。
- 列表按 `serviceDate/startTime/id` 倒序返回。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/TutoringAdminController.java`
- `ruoyi-system/src/main/resources/mapper/system/TutoringScheduleMapper.xml`
