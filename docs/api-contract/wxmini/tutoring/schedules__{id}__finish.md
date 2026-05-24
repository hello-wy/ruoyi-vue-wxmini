# POST /wxmini/tutoring/schedules/{id}/finish

## 用途

- `POST`：学生身份下提交上课签到。

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
- 仅 `status = 0` 的课表允许上课签到，否则返回：`当前课表状态不可签到`。
- 签到后状态变为 `1=正在上课`，并记录 `finishTime` / `finishRemark`。
- 该接口沿用历史字段名 `finishTime` / `finishRemark` 存储上课签到时间与备注。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxTutoringPayServiceImpl.java`
