# GET /system/lectures/{id}

## 用途

- `GET`：获取课程活动/讲座详细信息（含讲师信息，公开）。

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
    "coursePrice": 3980.00,
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
- `coursePrice`：课程全价，仅用于课程信息展示；不参与报名费、定金或任何支付金额计算。
- `cover`：讲座详情海报数量。
- `coverId`：讲座封面资源目录 ID。

### 失败场景或特殊说明

- 以当前 controller/service 的实际校验结果为准。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/LecturesController.java`
