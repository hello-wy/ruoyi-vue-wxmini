# GET /system/lectures/list

## 用途

- `GET`：查询课程活动/讲座列表（含讲师姓名）。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:lectures:list')`。

## 请求头

- `Authorization: Bearer <token>`

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
      "coursePrice": 3980.00,
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
- `coursePrice`：课程全价，仅用于课程信息展示；不参与报名费、定金或任何支付金额计算。
- `requiresEnrollment`：是否报名前需要该课程学籍；缺省或 `true` 表示需要，`false` 表示不需要。
- `cover`：讲座详情海报数量。
- `coverId`：讲座封面资源目录 ID。

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 无权限时，请求会被拦截。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/LecturesController.java`
