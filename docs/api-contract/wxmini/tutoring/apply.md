# POST /wxmini/tutoring/apply

## 用途

- 申请成为教员，`status` 默认重置为 `0` 待审核。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`
- `Content-Type: application/json`

## Body 示例

```json
{
  "realName": "张三",
  "idCard": "320100200001010000",
  "school": "南京大学",
  "major": "数学",
  "identity": 0,
  "currentGrade": "大三",
  "city": "江宁区",
  "subjects": "8,10",
  "areas": "320115,320114",
  "methods": 1,
  "degree": 1,
  "certificateList": "教师资格证,英语六级",
  "certificates": "1234567890123456789,1234567890123456790"
}
```

## `certificates` 说明

- `certificates` 不再保存图片 URL，而是英文逗号分隔的材料 ID 字符串。
- 材料 ID 必须先通过 `POST /wxmini/tutoring/materials` 登记取得。
- 类型 `1`、`2`、`3` 各最多绑定一张；类型 `4` 可以绑定多张。
- 传空字符串表示不绑定审核材料。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "申请成功，请等待审核",
  "data": "1234567890123456789"
}
```

## 失败场景或特殊说明

- 未登录：`msg = 请先登录`。
- 用户不存在：`msg = 用户不存在`。
- 重复申请：`msg = 您已提交过申请，请勿重复提交`。
- `identity = 0` 时必须传 `currentGrade`，否则：`msg = 请选择当前年级`。
- 材料 ID 格式错误、材料不存在、材料属于其他用户或单张类型重复时，提交失败并返回明确错误消息。
- 创建时会同步更新当前用户实名信息，成功后写入教员记录并将状态重置为待审核。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/TutorMaterialServiceImpl.java`
