# GET /wxmini/growup/courses/{id}/reviews、POST /wxmini/growup/courses/{id}/reviews

## 用途

- `GET`：获取该课程下全部用户已发布的评价，供所有访客浏览。
- `POST`：当前登录且已报名课程的用户发布或更新自己的课程评价。

## 鉴权

- `GET`：公开接口，无需请求头。
- `POST`：小程序前台普通用户接口，请求头使用 `Wx-Authorization: Bearer <token>`。

## Path 参数

- `id`：课程/讲座 ID。

## GET 成功响应

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "id": 18,
      "courseId": 12,
      "content": "课程内容很实用，老师讲得清楚。",
      "reviewerName": "张三",
      "reviewerAvatarUrl": "https://example.com/avatar.webp",
      "createTime": "2026-07-12 10:00:00",
      "updateTime": "2026-07-12 10:00:00"
    }
  ]
}
```

## POST 请求体

```json
{
  "content": "课程内容很实用，老师讲得清楚。"
}
```

- `content`：必填，去除首尾空白后不能为空，最长 1000 字。

## POST 成功响应

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 18,
    "courseId": 12,
    "content": "课程内容很实用，老师讲得清楚。",
    "createTime": "2026-07-12 10:00:00",
    "updateTime": "2026-07-12 10:00:00"
  }
}
```

## 特殊说明

- 列表按最后更新时间倒序返回全部评价，不分页。
- 发布时后端根据当前登录用户和课程 ID 查找最近一笔 `status in (1, 2)` 的课程订单；未报名、未支付、已退款或已取消用户会返回“仅已报名课程可评价”。
- 同一课程订单重复发布时，会更新该订单已有评价；其他用户下次请求公开列表即可看到更新后的内容。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxGrowupController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxCourseReviewServiceImpl.java`
- `ruoyi-system/src/main/resources/mapper/system/CourseReviewMapper.xml`
