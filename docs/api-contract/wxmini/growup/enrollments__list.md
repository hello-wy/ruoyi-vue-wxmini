# GET /wxmini/growup/enrollments/list

## 用途

获取当前登录小程序用户的学籍列表。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`

## Query 参数

- 分页参数沿用若依：`pageNum`、`pageSize`

## 成功响应示例

```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 1,
  "rows": [
    {
      "lectureId": 1,
      "remain": 10
    }
  ]
}
```

## 特殊说明

- 若当前 token 对应用户不存在，返回空列表，不抛错。

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxGrowupController.java`
