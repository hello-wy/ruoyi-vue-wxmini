# POST /system/tutoring-admin/bindings

## 用途

- `POST`：后台为家长需求绑定教员，生成家教闭环的第一步绑定关系。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:parents:edit')`。

## 请求头

- `Authorization: Bearer <token>`

## Query 参数

- `parentId`：必填，家长需求 ID。
- `tutorId`：必填，教员 ID。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 20001,
    "parentId": 30001,
    "parentUserId": 101,
    "tutorId": 40001,
    "tutorUserId": 202,
    "status": 0
  }
}
```

## 失败场景或特殊说明

- `parentId` 或 `tutorId` 为空：`msg = 需求和教员不能为空`。
- 家长需求不存在：`msg = 家长需求不存在`。
- 教员不存在或未通过审核：`msg = 教员不存在或未通过审核`。
- 家长需求未关联平台用户：`msg = 家长需求未关联平台用户`。
- 教员用户不存在：`msg = 教员用户不存在`。
- 若相同 `parentId + tutorId` 已存在且未关闭，则直接返回现有绑定，不重复插入。
- 绑定时会快照 `parents.serviceTimes` 到 `serviceTimesSnapshot`。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/TutoringAdminController.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/TutoringBindingServiceSupport.java`
