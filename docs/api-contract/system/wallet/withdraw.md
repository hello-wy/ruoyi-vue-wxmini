# POST /system/wallet/withdraw

## 用途

- `POST`：申请提现到微信钱包（需登录，body 传 amount 字段）。

## 鉴权

- 需要 `Authorization: Bearer <token>`。

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- 无。

## POST 请求

### Query 参数

- 无。

### Body 示例

- 请求体为 `Object>` JSON 对象，字段以对应 BO / domain 定义为准。

### 成功响应示例

```json
{
  "code": 200,
  "msg": "提现申请已提交"
}
```

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 缺少金额：`msg = 提现金额不能为空`。
- 金额非法：`msg = 金额格式不正确`。
- 请求体只读取 `amount` 字段。
- 当前控制器位于 `ruoyi-wxmini` 模块，但接口路径仍是 `/system/wallet/**`，并通过 `SecurityUtils.getUserId()` 读取若依标准登录态。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxWalletController.java`
