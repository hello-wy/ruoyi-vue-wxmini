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
  "realName": "张三",
  "idCard": "110105199001011234"
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
- 姓名为空：`msg = 请填写真实姓名`。
- 身份证号格式错误：`msg = 请填写正确的18位身份证号`。
- 当阿里云返回不一致时：`data.matched = false`，`data.reason = 姓名或身份证信息不匹配，请重新填写`。
- 阿里云访问凭证通过部署环境变量 `ALIYUN_CLOUDAUTH_ACCESS_KEY_ID`、`ALIYUN_CLOUDAUTH_ACCESS_KEY_SECRET` 注入；缺失时：`msg = 实名认证服务未配置，请联系管理员`。
- 当阿里云接口异常时：`msg = 实名认证失败，请稍后重试`。
- 当实名认证结果写入 `user_info` 失败时：`msg = 实名认证信息保存失败，请稍后重试`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxRealVerifyServiceImpl.java`
