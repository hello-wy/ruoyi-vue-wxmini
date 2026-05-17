# POST /wxmini/common/uploadJobSignImage

## 用途

- `POST`：上传兼职签到图片，供小程序端兼职安排页提交签到凭证使用。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。
- 服务端使用当前登录小程序用户 ID 作为目录名。

## 请求

- `Content-Type: multipart/form-data`
- 表单字段：
  - `file`：必填，图片文件。

## 文件限制

- 仅支持 `jpg`、`jpeg`、`png` 后缀。
- 文件保存目录：`{RuoYiConfig.profile}/job-sign/{userId}/`。
- 对外访问路径：`/profile/job-sign/{userId}/{fileName}`。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "url": "https://zhiyujia.xyz/profile/job-sign/321/1715832000000.jpg",
  "fileName": "/profile/job-sign/321/1715832000000.jpg",
  "newFileName": "1715832000000.jpg",
  "originalFilename": "sign.jpg"
}
```

## 失败场景或特殊说明

- 未登录返回业务错误：`请先登录`。
- 文件为空返回业务错误：`上传文件不能为空`。
- 后缀不支持返回业务错误：`仅支持上传 JPG、JPEG、PNG 格式图片`。
- 返回的 `fileName` 是后续 `POST /wxmini/jobs/orders/{orderNo}/sign-image` 的 `signImageUrl` 入参。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxCommonController.java`
