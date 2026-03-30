# GET /wxmini/tutoring/parents/list

## 用途

分页查询有效家教单列表。

## 鉴权

- 公开接口

## Query 参数

- `pageNum`：默认 1
- `pageSize`：默认 10
- `subject`：可选
- `region`：可选
- `methods`：可选
- `grade`：可选

## 成功响应示例

```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 1,
  "rows": [
    {
      "id": 1,
      "status": 0
    }
  ]
}
```

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
