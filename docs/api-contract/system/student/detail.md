# GET /system/student/{id}

## 用途
查询管理后台学员详情，用于学员详情页“基本信息”tab。

## 鉴权
- 需要 `Authorization: Bearer <token>`
- 需要具备权限标识：`system:student:query`

## 路径参数
- `id`: `user_info.id`

## 成功响应示例
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "userId": "uuid-1",
    "displayName": "张三",
    "userName": "zhangsan",
    "phone": "13800138000",
    "realName": "张三",
    "nickName": "小张",
    "gender": 0,
    "age": 48,
    "userType": 3,
    "userTypeLabel": "阿姨",
    "companyName": "",
    "companyAddress": "",
    "companyPosition": "",
    "industry": "",
    "workYears": "",
    "personalIntro": ""
  }
}
```
