# GET /wxmini/jobs/{jobId}/signup-users

## 用途

- `GET`：获取指定岗位已支付报名用户池，供商家查询报名人员并发工资时选择员工。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。
- 仅岗位发布商家可访问。

## Query 参数

- `keyword`：可选，按姓名 / 昵称 / 手机号搜索。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "userInfoId": 2001,
      "displayName": "张三",
      "phoneMasked": "138****8000",
      "payrollPaid": true,
      "payrollItemStatus": 1
    }
  ]
}
```

## 失败场景或特殊说明

- 非商家身份访问会返回业务错误。
- 非岗位发布商家访问会返回业务错误。
- 仅返回该岗位已支付报名用户。
- `payrollPaid = true` 表示该用户在当前岗位下已有工资入账记录，前端应展示为“已支付/不可重复加入批次”。
- `payrollItemStatus = 1` 表示已发工资，`0` 表示未发工资。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxJobScheduleController.java`
