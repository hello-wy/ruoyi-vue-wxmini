# GET /system/wallet/withdrawRecords

## 用途

- `GET`：获取当前登录用户的提现记录列表（需登录）。

## 鉴权

- 需要 `Authorization: Bearer <token>`。

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- 无。

## GET 请求

### Query 参数

- 无。

### Body 示例

- 无。

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "id": 1,
      "amount": 50.00
    }
  ]
}
```

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 当前控制器位于 `ruoyi-wxmini` 模块，但接口路径仍是 `/system/wallet/**`，并通过 `SecurityUtils.getUserId()` 读取若依标准登录态。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxWalletController.java`
