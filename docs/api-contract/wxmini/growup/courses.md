# GET /wxmini/growup/courses

## 用途

- `GET`：获取全部课程活动/讲座列表（公开，附带讲师姓名）。

## 鉴权

- 公开接口。

## 请求头

- 无。

## Path 参数

- 无。

## GET 请求

### Query 参数

- 无分页参数；接口始终返回全部活动。
- 可选筛选字段沿用 `Lectures` 对象。

### Body 示例

- 无。

### 成功响应示例

```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 1,
  "rows": [
    {
      "id": 12,
      "name": "幸福解码",
      "registrationFee": 100.00,
      "coursePrice": 3980.00,
      "requiresEnrollment": true,
      "cover": "6",
      "coverId": 9,
      "isTop": true,
      "speakerNames": "黄老师"
    }
  ]
}
```

### 响应字段补充说明

- `id`：讲座记录主键。
- `registrationFee`：课程报名费，默认 `100.00`。
- `coursePrice`：课程全价，可能为 `null`；仅用于展示，不参与报名费、定金或任何支付金额计算。
- `requiresEnrollment`：是否报名前需要当前用户拥有该课程可用学籍；默认 `true`。
- `cover`：讲座详情海报数量。
- `coverId`：讲座封面资源目录 ID；前端封面图应优先使用该字段拼接 `/lectures/{coverId}/cover.webp`。
- `isTop`：是否在小程序首页「最近课程」区域置顶展示；`true` 时首页优先选择该活动作为最近课程，其他列表位置仍保持默认时间排序。

### 失败场景或特殊说明

- 接口返回全部活动，不按服务器当前时间或月份过滤。
- 结果按开讲时间升序返回。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxGrowupController.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/LecturesServiceImpl.java`
- `ruoyi-system/src/main/resources/mapper/system/LecturesMapper.xml`
