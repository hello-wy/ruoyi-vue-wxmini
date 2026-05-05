# POST /wxmini/jobs/{jobId}/status

## 用途

- `POST`：商家修改自己发布岗位的招聘状态。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。
- 仅岗位发布商家可访问。

## Path 参数

- `jobId`：岗位 ID。

## Body 示例

```json
{
  "status": 3
}
```

## 状态约束

- 仅允许两种变更：
  - `0 -> 3`：招聘中 -> 已取消
  - `3 -> 0`：已取消 -> 招聘中
- 其他状态流转会返回业务错误：`当前岗位状态不可修改`。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

## 失败场景或特殊说明

- 未登录返回：`请先登录`。
- 非商家身份返回业务错误。
- 非岗位发布商家访问返回业务错误。
- `status` 不能为空。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxJobScheduleController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxJobScheduleServiceImpl.java`
