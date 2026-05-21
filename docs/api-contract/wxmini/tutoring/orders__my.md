# GET /wxmini/tutoring/orders/my

## 用途

- `GET`：查询当前登录家长自己的家教订单列表。

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
      "id": 50001,
      "orderNo": "TUTOR202605191230001234",
      "bindingId": 20001,
      "parentId": 30001,
      "parentUserId": 101,
      "tutorId": 40001,
      "tutorUserId": 202,
      "lessonCount": 2,
      "hourlyPrice": 120.00,
      "totalAmount": 480.00,
      "commissionRate": 10.00,
      "status": 1,
      "payTime": "2026-05-19 12:35:00",
      "parentName": "周内晚间数学作业辅导",
      "tutorName": "王教员"
    }
  ]
}
```

## 失败场景或特殊说明

- 当前实现按 `parentUserId` 倒序返回全部家教订单。
- 返回结构为对象列表，不走若依分页包装。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
- `ruoyi-system/src/main/resources/mapper/system/TutoringOrderMapper.xml`
