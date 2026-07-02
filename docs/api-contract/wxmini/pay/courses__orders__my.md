# GET /wxmini/pay/courses/orders/my

## 用途

查询当前小程序用户课程报名订单，数据来源为 `course_pay_order`，并补充课程展示字段供小程序“我的课程”列表使用。

## 鉴权

- `Wx-Authorization: Bearer <token>`

## 请求头

| 名称 | 必填 | 说明 |
| --- | --- | --- |
| `Wx-Authorization` | 是 | 小程序登录 token，格式 `Bearer <token>` |

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "orderNo": "CRS2026070210000012345678901",
      "courseId": 18,
      "courseName": "幸福解码",
      "courseTime": "2026-07-10 09:00:00",
      "courseEndDate": "2026-07-10 17:00:00",
      "courseLocation": "南京市示例地址",
      "courseCover": "3",
      "courseCoverId": 18,
      "name": "张三",
      "gender": "男",
      "phone": "13800000000",
      "company": "示例单位",
      "accommodation": "不需要住宿",
      "enrollmentId": 6,
      "amount": 99.00,
      "status": 1,
      "payTime": "2026-07-02 10:05:00",
      "signTime": null,
      "refundTime": null,
      "createTime": "2026-07-02 10:00:00"
    }
  ]
}
```

## 成功响应字段

- `orderNo`：课程订单号
- `courseId`：课程 ID
- `courseName`：课程名称
- `courseTime`：课程开始时间，来自 `lectures.time`
- `courseEndDate`：课程结束时间，来自 `lectures.end_date`
- `courseLocation`：课程地点，来自 `lectures.location`
- `courseCover`：课程封面/详情图数量字段，来自 `lectures.cover`
- `courseCoverId`：课程封面资源目录 ID，来自 `lectures.cover_id`
- `name`：报名姓名
- `gender`：性别
- `phone`：联系电话
- `company`：单位
- `accommodation`：住宿需求
- `enrollmentId`：学籍/报名关联 ID
- `amount`：支付金额
- `status`：订单状态
- `payTime`：支付时间
- `signTime`：签到时间
- `refundTime`：退款时间
- `createTime`：订单创建时间

## 状态枚举

- `0`：待支付
- `1`：已支付待签到
- `2`：已签到
- `3`：已退款
- `4`：已取消

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxPayController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxCoursePayServiceImpl.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/CoursePayOrderServiceImpl.java`
