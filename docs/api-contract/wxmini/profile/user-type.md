# PUT /wxmini/profile/user-type

## 用途
切换当前登录用户身份，仅允许在家长(`0`)和学生(`1`)之间切换。

## 鉴权
- 需要 `Wx-Authorization: Bearer <token>`

## 请求体
```json
{
  "userType": "1"
}
```
