# POST /wxmini/jobs

## 用途
商家发布招聘信息。

## 鉴权
- 需要 `Wx-Authorization: Bearer <token>`
- 仅商家身份(`userType=2`)允许调用

## 请求体
```json
{
  "title": "初中数学辅导日结兼职",
  "category": "0",
  "salaryDay": "300",
  "workDate": "2026-04-20",
  "workTime": "09:00-12:00",
  "location": "南京市鼓楼区xx路xx号",
  "districtId": "320106",
  "contacts": "王老师",
  "phone": "13800138000",
  "description": "负责初中数学辅导"
}
```
