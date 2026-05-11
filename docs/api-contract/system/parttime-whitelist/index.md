# POST /system/parttime-whitelist

## 用途

- 管理员新增全局兼职报名白名单记录。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要管理员角色：`@ss.hasRole('admin')`。

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- 无。

## POST 请求

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

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/ParttimeSignupWhitelistController.java`
