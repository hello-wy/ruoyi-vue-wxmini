# POST /wxmini/jobs

## 用途

- `POST`：商家发布招聘。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

## Path 参数

- 无。

## POST 请求

### Query 参数

- 无。

### Body 示例

```json
{
  "title": "初中数学辅导日结兼职",
  "category": 0,
  "salaryDay": 300,
  "workDate": "2026-04-20",
  "workTime": "09:00-12:00",
  "location": "南京市鼓楼区xx路xx号",
  "contacts": "王老师",
  "phone": "13800138000",
  "description": "负责初中数学辅导",
  "signupLimit": 10
}
```

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": 123456789012345678
}
```

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 非商家身份：`msg = 仅商家可以发布招聘`。
- 缺少必填字段时，会返回对应中文提示。
- 仅 `userType = 2` 的商家身份允许发布招聘。
- 创建时会校验标题、分类、日薪、工作日期、工作时段、地址、联系人、手机号、描述与报名人数。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxDailyJobController.java`
