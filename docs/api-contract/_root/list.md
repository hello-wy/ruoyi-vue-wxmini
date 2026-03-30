# GET /list

## 用途

查询兼职日结工作分页列表。

## 鉴权

- 公开接口

## Query 参数

- 分页参数沿用若依：`pageNum`、`pageSize`
- 其他筛选字段来自 `DailyJobs` 实体

## 成功响应示例

```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 1,
  "rows": [
    {
      "id": 1,
      "title": "兼职岗位"
    }
  ]
}
```

## 特殊说明

- 当前路径是根路径 `/list`，不是 `/wxmini/dailyJobs/list`。

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxDailyJobController.java`
