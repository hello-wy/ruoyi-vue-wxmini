# GET /wxmini/growup/courses/{id}

## 用途

- `GET`：获取课程/讲座详细信息（公开，附带讲师信息）。

## 鉴权

- 公开接口。

## 请求头

- 无。

## Path 参数

- `id`：路径参数。

## GET 请求

### Query 参数

- 无。

### Body 示例

- 无。

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 12,
    "name": "幸福解码",
    "time": "2026-06-12 09:00",
    "endDate": "2026-06-13 18:00",
    "location": "北京",
    "registrationFee": 100.00,
    "deposit": 30.00,
    "requiresEnrollment": true,
    "enrolledCount": 18,
    "cover": "6",
    "coverId": 9,
    "speakers": [
      {
        "id": 3,
        "name": "黄老师",
        "avatarUrl": "https://example.com/avatar.webp"
      }
    ]
  }
}
```

### 响应字段补充说明

- `id`：讲座记录主键。
- `time`：开课开始时间，格式 `yyyy-MM-dd HH:mm`。
- `endDate`：开课结束时间，格式 `yyyy-MM-dd HH:mm`。
- `location`：上课地址。
- `registrationFee`：课程报名费，默认 `100.00`。
- `cover`：讲座详情海报数量。
- `coverId`：讲座封面资源目录 ID；顶部封面图应优先使用该字段拼接 `/lectures/{coverId}/cover.webp`。
- `deposit`：席位预定金/报名押金，报名接口会以后端课程配置中的该字段作为微信支付金额来源；缺失时报名接口返回错误。
- `requiresEnrollment`：是否报名前需要当前用户拥有该课程可用学籍；默认 `true`。
- `enrolledCount`：课程已报名人数，来源于 `lectures.enrolled_count`；成长课程支付成功首次回调后加 `1`。

### 失败场景或特殊说明

- 以当前 controller/service 的实际校验结果为准。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxGrowupController.java`
