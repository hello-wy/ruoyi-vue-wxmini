# POST /wxmini/common/uploadJobSignImage

## 用途

- 上传兼职签到图片，供后续提交签到材料时引用。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`
- `Content-Type: multipart/form-data`

## Path 参数

- 无。

## Query 参数

- 无。

## Body 参数

- `jobId`：必填，兼职岗位 ID，表单字段。
- `file`：必填，签到图片文件，仅支持 `jpg`、`jpeg`、`png`。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "url": "https://api.example.com/profile/sign/job/12/20260513_xxx.png",
  "fileName": "/profile/sign/job/12/20260513_xxx.png",
  "newFileName": "20260513_xxx.png",
  "originalFilename": "signin.png"
}
```

## 失败场景或特殊说明

- 未登录时返回：`请先登录`。
- `jobId` 为空时返回：`岗位不能为空`。
- 文件为空时返回：`上传文件不能为空`。
- 文件格式非法时返回：`仅支持上传 JPG、JPEG、PNG 格式图片`。
- 文件保存目录为：`{profile}/sign/job/{jobId}`。
- 前端提交签到时应优先使用返回的 `fileName`，即相对路径 `/profile/...`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxCommonController.java`
