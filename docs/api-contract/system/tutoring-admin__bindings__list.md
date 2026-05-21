# GET /system/tutoring-admin/bindings/list

## 用途

- `GET`：后台分页查询家教绑定列表。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:parents:list')`。

## 请求头

- `Authorization: Bearer <token>`

## Query 参数

- `pageNum` / `pageSize`：若依标准分页参数。
- `status`：可选，绑定状态筛选。
- `parentId`：可选，按家长需求筛选。
- `tutorId`：可选，按教员筛选。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 1,
  "rows": [
    {
      "id": 20001,
      "parentId": 30001,
      "parentUserId": 101,
      "tutorId": 40001,
      "tutorUserId": 202,
      "status": 1,
      "parentName": "周内晚间数学作业辅导",
      "tutorName": "王教员"
    }
  ]
}
```

## 失败场景或特殊说明

- 列表按创建时间倒序返回。
- 状态约定：`0=待下单`、`1=已下单`、`2=已关闭`。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/TutoringAdminController.java`
- `ruoyi-system/src/main/resources/mapper/system/TutoringBindingMapper.xml`
