# /system/parttime-whitelist

## 用途

- 管理员新增全局兼职报名白名单记录。
- 管理员分页查询兼职报名白名单，支持按姓名、身份证模糊搜索。
- 管理员删除单条兼职报名白名单记录。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要管理员角色：`@ss.hasRole('admin')`。

## 请求头

- `Authorization: Bearer <token>`

## POST /system/parttime-whitelist

### Query 参数

- 无。

### Body 示例

```json
{
  "realName": "张三",
  "idCard": "11010519900101123X",
  "price": 88.5,
  "remark": "商家线下确认"
}
```

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": 192837465001
}
```

### 失败场景或特殊说明

- `realName` 为空时返回：`请填写真实姓名`。
- `idCard` 非 18 位身份证格式时返回：`请填写正确的18位身份证号`。
- 同一身份证重复录入时返回：`该身份证已存在白名单记录`。
- 服务端会自动 trim `realName`、`idCard`、`remark`，并将身份证尾号 `x` 规范化为大写 `X`。
- 未传 `status` 时服务端默认写入 `1`（启用）。

## GET /system/parttime-whitelist/list

### Query 参数

- `pageNum`：页码，可选，默认 `1`。
- `pageSize`：每页条数，可选，默认 `10`。
- `realName`：真实姓名模糊搜索，可选。
- `idCard`：身份证号模糊搜索，可选。

### 成功响应示例

```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 1,
  "rows": [
    {
      "id": 192837465001,
      "realName": "张三",
      "idCard": "11010519900101123X",
      "price": 88.5,
      "status": 1,
      "remark": "商家线下确认",
      "createBy": "admin",
      "createTime": "2026-05-11 22:30:00",
      "updateBy": null,
      "updateTime": null
    }
  ]
}
```

### 特殊说明

- `realName` 与 `idCard` 均为模糊匹配。
- 返回结构为分页列表：`{ code, msg, total, rows }`。
- 默认按 `create_time desc, id desc` 排序。

## DELETE /system/parttime-whitelist/{id}

### Path 参数

- `id`：白名单记录 ID，必填。

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 失败场景或特殊说明

- 当 `id` 不存在时，返回默认失败响应：`{ code: 500, msg: "操作失败" }`。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/ParttimeSignupWhitelistController.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/ParttimeSignupWhitelistServiceImpl.java`
- `ruoyi-system/src/main/resources/mapper/system/ParttimeSignupWhitelistMapper.xml`
