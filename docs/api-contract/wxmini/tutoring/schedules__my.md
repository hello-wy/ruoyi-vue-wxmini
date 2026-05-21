# GET /wxmini/tutoring/schedules/my

## 用途

- `GET`：查询当前登录用户自己的家教课表。
- 家长看到自己下单后的课表；教员看到分配给自己的课表。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

## Query 参数

- 无。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "id": 60001,
      "orderId": 50001,
      "orderNo": "TUTOR202605191230001234",
      "bindingId": 20001,
      "parentId": 30001,
      "parentUserId": 101,
      "tutorId": 40001,
      "tutorUserId": 202,
      "serviceDate": "2026-05-20",
      "startTime": "18:00",
      "endTime": "20:00",
      "hours": 2.00,
      "grossAmount": 240.00,
      "commissionAmount": 24.00,
      "netAmount": 216.00,
      "status": 1,
      "finishTime": "2026-05-20 20:10:00",
      "finishRemark": "已上课",
      "confirmTime": null,
      "confirmRemark": null,
      "parentName": "周内晚间数学作业辅导",
      "tutorName": "王教员",
      "location": "百家湖 88号 1单元"
    }
  ]
}
```

## 失败场景或特殊说明

- 用户不存在：`msg = 用户不存在`。
- 当 `userType = 0` 时按家长 `parentUserId` 查询；其它身份按教员 `tutorUserId` 查询。
- 状态约定：`0=待上课`、`1=待家长确认`、`2=待结算`、`3=已结算`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
- `ruoyi-system/src/main/resources/mapper/system/TutoringScheduleMapper.xml`
