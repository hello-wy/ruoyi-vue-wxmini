# GET /wxmini/growup/courses

## 用途

- `GET`：获取课程/讲座列表（公开，附带讲师姓名）。

## 鉴权

- 公开接口。

## 请求头

- 无。

## Path 参数

- 无。

## GET 请求

### Query 参数

- `pageNum`：若依标准分页页码。
- `pageSize`：若依标准分页每页条数。
- 其余筛选字段沿用 `Lectures` 对象。

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
      "requiresEnrollment": true,
      "cover": "6",
      "coverId": 9,
      "speakerNames": "黄老师"
    }
  ]
}
```

### 响应字段补充说明

- `id`：讲座记录主键。
- `registrationFee`：课程报名费，默认 `100.00`。
- `requiresEnrollment`：是否报名前需要当前用户拥有该课程可用学籍；默认 `true`。
- `cover`：讲座详情海报数量。
- `coverId`：讲座封面资源目录 ID；前端封面图应优先使用该字段拼接 `/lectures/{coverId}/cover.webp`。

### 失败场景或特殊说明

- 以当前 controller/service 的实际校验结果为准。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxGrowupController.java`
