# POST /wxmini/tutoring/mine/update

## 用途

- 更新当前登录用户的教员资料，并重新提交审核。

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
  "identity": 0,
  "currentGrade": "大四",
  "school": "南京大学",
  "major": "数学",
  "subjects": "8,10",
  "areas": "320115,320114",
  "methods": 1,
  "degree": 1,
  "certificateList": "教师资格证,英语六级",
  "certificates": "1234567890123456789,1234567890123456790"
}
```

## `certificates` 说明

- 值为通过 `POST /wxmini/tutoring/materials` 取得的材料 ID 列表，使用英文逗号分隔。
- 提交后的列表是最终保留集合；不在列表中的旧材料会从该教员记录中删除。
- 传空字符串会清空该教员的全部审核材料。
- 类型 `1`、`2`、`3` 各最多一张，类型 `4` 可以多张。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "资料已更新，请等待审核"
}
```

## 失败场景或特殊说明

- 未登录：`msg = 请先登录`。
- 尚未申请：`msg = 请先提交申请`。
- 用户不存在：`msg = 用户不存在`。
- 材料 ID 无效、非当前用户所有、已绑定其他教员或单张类型重复时，更新失败。
- 更新成功后审核状态重置为 `0`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
