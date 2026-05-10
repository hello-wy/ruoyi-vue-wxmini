# GET /system/lectures/recent

## 用途

- `GET`：查询最近一个月课程活动/讲座列表（公开）。

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
      "cover": "6",
      "coverId": 9
    }
  ]
}
```

### 响应字段补充说明

- `id`：讲座记录主键。
- `cover`：讲座详情海报数量。
- `coverId`：讲座封面资源目录 ID。

### 失败场景或特殊说明

- 以当前 controller/service 的实际校验结果为准。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/LecturesController.java`
