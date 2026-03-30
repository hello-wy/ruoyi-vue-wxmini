# POST /wxmini/faceAuth/init

## 用途

初始化阿里云实人认证，返回 `certifyId` 和认证跳转信息。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`

## Body 示例

```json
{
  "name": "张三",
  "certNo": "110101199001011234",
  "returnUrl": "https://example.com/face-auth/callback",
  "metaInfo": "aliyun-meta-info"
}
```

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "certifyId": "xxxxx",
    "certifyUrl": "https://..."
  }
}
```

## 失败说明

- 未登录：`msg = not login`
- 姓名为空：`msg = name required`
- 身份证为空：`msg = certNo required`

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxFaceAuthController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/bo/FaceAuthInitBO.java`
