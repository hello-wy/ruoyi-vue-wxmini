# GET /wxmini/growup/courses/{id}/reviews/my、POST /wxmini/growup/courses/{id}/reviews/my

## 用途

兼容已有“我的课程”页面：按指定课程订单查询或保存当前登录用户的课程评价。

## 鉴权

小程序前台普通用户接口，请求头使用 `Wx-Authorization: Bearer <token>`。

## Path 参数

- `id`：课程/讲座 ID。

## GET Query 参数

- `orderNo`：必填，当前用户的课程订单号。

## POST 请求体

```json
{
  "orderNo": "COURSE202607120001",
  "content": "课程内容很实用，老师讲得清楚。"
}
```

- `orderNo`：必填，必须属于当前登录用户，且与路径中的课程 ID 一致。
- `content`：必填，去除首尾空白后不能为空，最长 1000 字。

## 成功响应

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 18,
    "orderNo": "COURSE202607120001",
    "courseId": 12,
    "content": "课程内容很实用，老师讲得清楚。",
    "createTime": "2026-07-12 10:00:00",
    "updateTime": "2026-07-12 10:00:00"
  }
}
```

未提交过评价时，`GET` 的 `data` 为 `null`。

## 特殊说明

仅课程订单状态为 `1`（已报名/待签到）或 `2`（已签到）时可查询、保存评价。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxGrowupController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxCourseReviewServiceImpl.java`
