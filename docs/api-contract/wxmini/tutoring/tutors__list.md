# GET /wxmini/tutoring/tutors/list

## 用途

分页查询已认证教员列表。

## 鉴权

- 公开接口

## Query 参数

- `pageNum`：默认 1
- `pageSize`：默认 5
- `subject`：可选，科目模糊查询
- `region`：可选，区域模糊查询
- `methods`：可选，授课方式
- `grade`：可选，学历

## 成功响应示例

```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 1,
  "rows": [
    {
      "id": 1,
      "uid": 1,
      "isCertified": 1
    }
  ]
}
```

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
