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
- `cover`：讲座详情海报数量。
- `coverId`：讲座封面资源目录 ID；顶部封面图应优先使用该字段拼接 `/lectures/{coverId}/cover.webp`。

### 失败场景或特殊说明

- 以当前 controller/service 的实际校验结果为准。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxGrowupController.java`
