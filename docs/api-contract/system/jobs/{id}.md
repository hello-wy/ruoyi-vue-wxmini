# GET /system/jobs/{id}

## 用途

- `GET`：获取兼职日结工作详细信息。

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
    "id": 1
  }
}
```

### 失败场景或特殊说明

- 以当前 controller/service 的实际校验结果为准。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/DailyJobsController.java`
