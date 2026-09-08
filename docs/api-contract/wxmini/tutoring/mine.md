# GET /wxmini/tutoring/mine

## 用途

- 查看当前登录用户的教员资料和审核材料。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": "1234567890123456700",
    "uid": "wx-user-123",
    "status": 0,
    "certificates": "1234567890123456789,1234567890123456790",
    "materials": [
      {
        "id": "1234567890123456789",
        "type": 1,
        "typeName": "身份证正面",
        "directoryName": "sfz_front",
        "url": "/profile/certification/321/sfz_front/sfz_front-uuid.jpg"
      },
      {
        "id": "1234567890123456790",
        "type": 4,
        "typeName": "证书",
        "directoryName": "certification",
        "url": "/profile/certification/321/certification/certification-uuid.jpg"
      }
    ]
  }
}
```

## 响应字段说明

- `certificates`：英文逗号分隔的材料 ID 列表，不是图片 URL。
- `materials`：按 `certificates` 中的 ID 顺序返回材料详情。
- `materials[].type`：`1` 身份证正面、`2` 身份证反面、`3` 学生证、`4` 证书。
- `materials[].typeName`：材料类型中文名称。
- `materials[].directoryName`：材料对应存储目录名称。

## 失败场景或特殊说明

- 未登录：`msg = 请先登录`。
- 当前用户尚未提交教员申请时，`data` 为 `null`。
- 此接口仅返回当前用户自己的敏感审核材料。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
