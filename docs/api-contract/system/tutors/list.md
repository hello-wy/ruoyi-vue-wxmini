# GET /system/tutors/list

## 用途

分页查询教员列表。可通过 `isCertified=0` 查询待审核教员列表。

## 鉴权

- 需要 `Authorization: Bearer <token>`
- 需要具备权限标识：`system:tutors:list`

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- 无

## Query 参数

- `pageNum`：页码，默认由后端分页插件处理
- `pageSize`：每页条数，默认由后端分页插件处理
- `orderByColumn`：可选，排序字段，例如 `createDate`
- `isAsc`：可选，排序方向，例如 `desc`
- `isCertified`：可选，教员审核状态，`0` 待审核、`1` 已通过、`2` 已拒绝

## 成功响应示例

```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 1,
  "rows": [
    {
      "id": 1,
      "uid": 10001,
      "realName": "张三",
      "school": "南京大学",
      "major": "数学",
      "methods": 1,
      "certificate": "教师资格证",
      "certificates": "https://example.com/cert.jpg",
      "isCertified": 0
    }
  ]
}
```

## 特殊说明

- 返回结构为分页对象 `{ code, msg, total, rows }`。
- 该接口对应后台管理侧教员列表，不是 `/wxmini/**` 小程序接口。

## 实现来源

- `ruoyi-vue-wxmini/ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/TutorsController.java`
