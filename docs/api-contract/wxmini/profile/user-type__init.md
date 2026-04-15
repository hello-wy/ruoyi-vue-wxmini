# POST /wxmini/profile/user-type/init

## 用途
首次设置当前登录用户身份，仅允许选择家长(`0`)或学生(`1`)。

## 鉴权
- 需要 `Wx-Authorization: Bearer <token>`

## 请求体
```json
{
  "userType": 0
}
```

## 字段说明
- `userType`: number，`0`=家长，`1`=学生；普通切换入口不允许传 `2`
