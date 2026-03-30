# GET /wxmini/faceAuth/query

## 用途

查询实人认证结果。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`

## Query 参数

- `certifyId`：必填，由初始化接口返回

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "passed": true
  }
}
```

## 失败说明

- 未登录：`msg = not login`
- 缺少参数：`msg = certifyId required`

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxFaceAuthController.java`
