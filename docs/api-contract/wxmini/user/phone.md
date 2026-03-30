# POST /wxmini/user/phone

## 用途

在已获得 `Wx-Authorization` 登录态后，使用微信实时手机号 `phoneCode` 拉取当前用户手机号并回写到 `user_info.phone`。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`

## 请求头

- `Content-Type: application/json`
- `Wx-Authorization: Bearer <token>`

## Path 参数

- 无

## Query 参数

- 无

## Body 示例

```json
{
  "appid": "wx1234567890",
  "phoneCode": "1234567890abcdef"
}
```

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "phone": "13800000000"
  }
}
```

## 失败场景或特殊说明

- `code != 200` 时，`msg` 可能为：
  - `empty request body`
  - `empty phoneCode`
  - `user not found`
  - `can not find appid=[xxx] config`
- 当前实现基于 `weixin-java-miniapp 4.7.0` 的 `WxMaUserService#getPhoneNumber(String)` 解析 `phoneCode`
- 接口只返回收口后的业务 VO，不直接暴露微信 SDK 的手机号 bean

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxMaUserController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/bo/WxPhoneCodeRequest.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/vo/WxPhoneInfoVO.java`
