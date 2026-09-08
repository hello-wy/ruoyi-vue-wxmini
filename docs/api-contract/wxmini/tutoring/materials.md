# POST /wxmini/tutoring/materials

## 用途

登记一张已通过对应类型独立上传接口保存的教员审核材料，返回材料 ID。本接口只保存图片相对 URL、材料类型和所属用户。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。
- `Content-Type: application/json`。

## Body 字段

- `type`：材料类型，必填。
  - `1`：身份证正面，仅允许在最终提交中保留一张。
  - `2`：身份证反面，仅允许在最终提交中保留一张。
  - `3`：学生证，仅允许在最终提交中保留一张。
  - `4`：证书，允许多张。
- `url`：对应上传接口返回的 `fileName` 相对资源路径，必填，最大 512 个字符。

URL 必须属于当前登录用户，并与 `type` 的目录和文件名前缀匹配。例如类型 `1` 必须使用：

```text
/profile/certification/{当前用户ID}/sfz_front/sfz_front-{唯一标识}.jpg
```

## Body 示例

```json
{
  "type": 1,
  "url": "/profile/certification/321/sfz_front/sfz_front-uuid.jpg"
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
    "typeName": "身份证正面",
    "directoryName": "sfz_front",
    "url": "/profile/certification/321/sfz_front/sfz_front-uuid.jpg"
  }
}
```

## 使用流程

1. 按材料类型调用 `material-uploads.md` 中对应的独立上传接口。
2. 使用上传响应的 `type` 和 `fileName` 调用本接口登记材料，取得字符串形式的材料 ID。
3. 申请或更新教员资料时，将所有保留材料的 ID 用英文逗号连接后写入 `certificates`。

## 失败场景或特殊说明

- 未登录：`msg = 请先登录`。
- `type` 不在 `1` 至 `4`：`msg = 材料类型非法`。
- URL 为空或超过 512 个字符：`msg = 材料图片地址非法`。
- URL 不属于当前用户，或目录、文件名前缀与 `type` 不匹配：`msg = 材料图片地址与类型不匹配`。
- 材料最终绑定到教员时，后端会校验材料归属、材料 ID 有效性，以及身份证正反面和学生证不能重复。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutorMaterialController.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/TutorMaterialServiceImpl.java`
