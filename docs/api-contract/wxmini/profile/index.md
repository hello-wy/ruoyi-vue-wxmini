# PUT /wxmini/profile

## 用途

- `PUT`：编辑当前登录用户个人资料。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

## Path 参数

- 无。

## PUT 请求

### Query 参数

- 无。

### Body 示例

```json
{
  "userName": "张三",
  "phone": "13800138000",
  "realName": "张三",
  "nickName": "小张",
  "gender": 1,
  "age": 28,
  "companyName": "智育傢",
  "companyAddress": "南京市鼓楼区",
  "companyPosition": "运营",
  "industry": "教育",
  "workYears": "5年",
  "personalIntro": "擅长家校沟通",
  "availableTime": "周末全天",
  "workExperience": "有家教与助教经验"
}
```

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 已完成实名认证的用户，姓名以实名认证写入的 `user_info.real_name` 为准，资料编辑不会修改实名姓名。
- 未实名认证时，资料编辑保存的姓名不作为实名认证状态或实名展示依据。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxUserProfileController.java`
