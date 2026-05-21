# POST /wxmini/tutoring/schedules/{id}/finish

## 用途

- `POST`：教员提交完课。

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
  "remark": "已完成本次上门辅导"
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
- 当前用户不是该课表教员：`msg = 无权操作该课表`。
- 仅 `status = 0` 的课表允许完课，否则返回：`当前课表状态不可完课`。
- 完课后状态变为 `1=待家长确认`，并记录 `finishTime` / `finishRemark`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxTutoringPayServiceImpl.java`
