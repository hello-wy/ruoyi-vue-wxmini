# POST /wxmini/tutoring/real-verify/verify

## 用途

- `POST`：调用阿里云身份二要素核验，校验姓名与身份证号是否一致（需登录）。
- 当核验成功时，后端会同步写入 `user_info.real_name`、`user_info.id_card`，并把 `user_info.is_realname_auth` 置为 `1`。

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
  "realName": "示例用户",
  "idCard": "11010519491231002X"
}
```

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "matched": true,
    "reason": ""
  }
}
```

### 失败场景或特殊说明

- 未登录：`msg = 请先登录`。
- 用户不存在：`msg = 用户不存在`。
- 姓名为空：`msg = 请填写真实姓名`；超过 50 个字符时：`msg = 真实姓名不能超过50个字符`。
- 身份证号格式错误：`msg = 请填写正确的18位身份证号`；出生日期错误时：`msg = 身份证号中的出生日期不正确`；校验位错误时：`msg = 身份证号校验位不正确`。
- 当阿里云返回不一致时：`data.matched = false`，`data.reason = 姓名或身份证信息不匹配，请重新填写`。
- 阿里云访问凭证通过部署环境变量 `ALIYUN_CLOUDAUTH_ACCESS_KEY_ID`、`ALIYUN_CLOUDAUTH_ACCESS_KEY_SECRET` 注入；缺失时：`msg = 实名认证服务未配置，请联系管理员`；授权失败时：`msg = 实名认证服务授权失败，请联系管理员`；余额不足时：`msg = 实名认证服务余额不足，请联系管理员`。
- 请求参数被阿里云拒绝时：`msg = 实名认证请求参数无效，请检查姓名和身份证号`；请求过于频繁时：`msg = 实名认证请求过于频繁，请稍后再试`；超时时：`msg = 实名认证服务响应超时，请稍后重试`；服务不可用时：`msg = 实名认证服务暂不可用，请稍后重试`；未知响应或状态异常时会明确返回对应的服务响应/结果状态异常提示。
- 当身份证号已被其他账号实名认证时：`msg = 该身份证号已被其他账号实名认证`；数据库锁冲突、数据约束、数据库不可用、其他数据库异常和未预期保存异常会分别返回对应提示。
- 数据库写入成功后才刷新用户缓存；写入失败不会把未落库的实名状态写入缓存。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxRealVerifyServiceImpl.java`
