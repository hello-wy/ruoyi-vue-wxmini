# POST /wxmini/tutoring/materials

## 用途

- 登记一张已经通过原上传接口保存的教员审核材料，返回材料 ID。
- 文件上传地址保持为 `POST /wxmini/common/uploadCertification`；本接口只保存图片 URL、材料类型和所属用户。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`
- `Content-Type: application/json`

## Body 字段

- `type`：材料类型，必填。
  - `1`：身份证正面，仅允许在最终提交中保留一张。
  - `2`：身份证反面，仅允许在最终提交中保留一张。
  - `3`：学生证，仅允许在最终提交中保留一张。
  - `4`：证书，允许多张。
- `url`：原上传接口返回的图片地址，必填，最大 512 个字符。

## Body 示例

```json
{
  "type": 1,
  "url": "/profile/upload/id-card-front.jpg"
}
```

## 成功响应示例

```json
{
  "code": 200,
  "msg": "材料登记成功",
  "data": {
    "id": "1234567890123456789",
    "type": 1,
    "url": "/profile/upload/id-card-front.jpg"
  }
}
```

## 使用流程

1. 调用 `POST /wxmini/common/uploadCertification` 上传图片并取得 URL。
2. 调用本接口登记 `type` 和 `url`，取得字符串形式的材料 ID。
3. 申请或更新教员资料时，将所有保留材料的 ID 用英文逗号连接后写入 `certificates`。

## 失败场景或特殊说明

- 未登录：`msg = 请先登录`。
- `type` 不在 `1` 至 `4`：`msg = 材料类型非法`。
- URL 为空、超长或不是 `/`、`http://`、`https://` 开头：`msg = 材料图片地址非法`。
- 材料最终绑定到教员时，后端会校验材料归属、材料 ID 有效性，以及身份证正反面和学生证不能重复。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutorMaterialController.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/TutorMaterialServiceImpl.java`
