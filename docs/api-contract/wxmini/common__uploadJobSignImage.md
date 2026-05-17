# POST /wxmini/common/uploadJobSignImage

## 用途

- 上传兼职签到图片，供小程序端兼职安排页提交签到凭证使用。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。
- 服务端使用当前登录小程序用户 ID 作为目录名。

## 请求头

- `Wx-Authorization: Bearer <token>`
- `Content-Type: multipart/form-data`

## Path 参数

- 无。

## Query 参数

- 无。

## Body 参数

- `file`：必填，签到图片文件，仅支持 `jpg`、`jpeg`、`png`。

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

- 未登录时返回：`请先登录`。
- 文件为空时返回：`上传文件不能为空`。
- 文件格式非法时返回：`仅支持上传 JPG、JPEG、PNG 格式图片`。
- 文件保存目录为：`{profile}/job-sign/{userId}`。
- 前端提交签到时应优先使用返回的 `fileName`，即相对路径 `/profile/...`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxCommonController.java`
