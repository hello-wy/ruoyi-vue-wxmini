# GET /wxmini/growup/courses

## 用途

获取课程或讲座分页列表，公开访问。返回值是若依分页结构。

## 鉴权

- 公开接口

## Query 参数

- 继承 `Lectures` 查询字段
- 分页参数沿用若依：`pageNum`、`pageSize`

## 成功响应示例

```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 1,
  "rows": [
    {
      "id": 1,
      "title": "课程标题",
      "questionnaire": []
    }
  ]
}
```

## 特殊说明

- 当前实现会为列表第一项补充最近问卷数据。
- 文档未声明的筛选字段，应以 `Lectures` 实体和 controller 当前实现为准。

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxGrowupController.java`
