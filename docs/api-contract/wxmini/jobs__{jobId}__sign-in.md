# POST /wxmini/jobs/{jobId}/sign-in

## 用途

- 提交兼职签到图片引用，创建或更新当前用户在该岗位下的签到记录。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`
- `Content-Type: application/json`

## Path 参数

- `jobId`：必填，兼职岗位 ID。

## Query 参数

- 无。

## Body 示例

```json
{
  "signImageUrl": "/profile/sign/job/12/20260513_xxx.png"
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

- 未登录时返回：`请先登录`。
- `jobId` 为空时抛出：`岗位不能为空`。
- `signImageUrl` 为空时抛出：`签到图片不能为空`。
- 当前用户不存在已支付报名订单时抛出：`未找到已支付报名订单`。
- 提交后会将签到记录置为待审核，并清空上一次审核备注、审核人、审核时间。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxJobScheduleController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxJobScheduleServiceImpl.java`
