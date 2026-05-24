# POST /wxmini/tutoring/schedules/{id}/confirm

## 用途

- `POST`：家长提交上课完成，等待管理员确认。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`
- `Content-Type: application/json`

## Path 参数

- `id`：课表 ID。

## Body 示例

```json
{
  "remark": "确认本次课程已完成"
}
```

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

## 失败场景或特殊说明

- 课表不存在：`msg = 课表不存在`。
- 当前用户不是该课表家长：`msg = 无权操作该课表`。
- 仅 `status = 1` 的课表允许提交上课完成，否则返回：`当前课表状态不可确认`。
- 若学生尚未上课签到，返回：`学生尚未上课签到`。
- 若家长已确认过，返回：`家长已提交确认，等待管理员审核`。
- 当前实现确认后保留 `status = 1`，同时写入 `confirmTime` / `confirmRemark`，前台以 `status = 1 && confirmTime != null` 展示为“待管理员审核”，再由后台审核流把课表推进到 `2=待结算`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxTutoringPayServiceImpl.java`
