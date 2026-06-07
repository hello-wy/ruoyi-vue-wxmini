# 商家身份白名单接口

## 用途

- 管理员审核商家身份申请。
- 后续业务按身份证号优先查询白名单记录，只有审核通过才允许使用商家身份。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要管理员角色：`@ss.hasRole('admin')`。

## 数据字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `id` | number | 白名单记录 ID，服务端 Snowflake 生成 |
| `realName` | string | 真实姓名 |
| `idCard` | string | 18 位身份证号，服务端按精确值查询 |
| `applyUserId` | string | 小程序申请用户 `userId` |
| `businessLicenseUrl` | string | 营业执照图片地址 |
| `status` | number | 审核状态：`0` 待审核，`1` 通过，`2` 拒绝；后台新增未传时默认 `1` |
| `remark` | string | 备注 |
| `createBy` | string | 创建人 |
| `createTime` | string | 创建时间 |
| `updateBy` | string | 更新人 |
| `updateTime` | string | 更新时间 |

## POST /system/merchant-user-type-whitelist

### 用途

- 管理员直接新增商家身份白名单记录。

### 请求头

- `Authorization: Bearer <token>`
- `Content-Type: application/json`

### Body 示例

```json
{
  "realName": "张三",
  "idCard": "11010519900101123X",
  "businessLicenseUrl": "/profile/merchant-license/user-1/a.jpg",
  "remark": "线下审核通过",
  "status": 1
}
```

### 服务端规范化

- `realName`：trim 前后空白。
- `idCard`：trim 前后空白，并将尾号 `x` 规范化为大写 `X`。
- `remark`：trim 前后空白；未传按空字符串写入。
- `status`：未传时默认写入 `1`；仅允许 `0`、`1` 或 `2`。
- `id`：服务端使用 Snowflake 生成。
- `createTime`：服务端写入当前时间。
- `updateTime`：新增时由数据库默认值写入。

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 失败场景

- `realName` 为空：`请填写真实姓名`。
- `idCard` 不是 `17` 位数字加最后一位数字或 `X`：`请填写正确的18位身份证号`。
- `status` 不是 `0`、`1` 或 `2`：`状态值只能为0、1或2`。
- 规范化后的 `idCard` 已存在：`该身份证已存在白名单记录`。

## GET /system/merchant-user-type-whitelist/list

### 用途

- 分页查询商家身份白名单列表。
- 身份证号是主查询条件，真实姓名可作为管理端辅助筛选。

### 请求头

- `Authorization: Bearer <token>`

### Query 参数

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `idCard` | string | 否 | 身份证号，服务端 trim 并转大写后按精确匹配查询 |
| `realName` | string | 否 | 真实姓名，服务端 trim 后模糊匹配 |
| `status` | number | 否 | 审核状态：`0` 待审核，`1` 通过，`2` 拒绝 |
| `pageNum` | number | 否 | 页码 |
| `pageSize` | number | 否 | 每页数量 |

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
      "applyUserId": "wx-user-1",
      "businessLicenseUrl": "/profile/merchant-license/wx-user-1/a.jpg",
      "status": 1,
      "remark": "线下审核通过",
      "createBy": "admin",
      "createTime": "2026-05-11 12:00:00",
      "updateBy": null,
      "updateTime": null
    }
  ]
}
```

## PUT /system/merchant-user-type-whitelist/{id}/audit

### 用途

- 管理员审核商家身份申请。
- 审核通过写入 `status = 1` 后，用户再次选择商家身份才可以切换成功。

### 请求头

- `Authorization: Bearer <token>`
- `Content-Type: application/json`

### Path 参数

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | number | 是 | 白名单记录 ID |

### Body 示例

```json
{
  "status": 1,
  "remark": "营业执照审核通过"
}
```

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 失败场景

- `status` 不是 `0`、`1` 或 `2`：`状态值只能为0、1或2`。

## DELETE /system/merchant-user-type-whitelist/{ids}

### 用途

- 删除一条或多条商家身份白名单记录。

### 请求头

- `Authorization: Bearer <token>`

### Path 参数

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ids` | string | 是 | 白名单 ID，多个用英文逗号分隔，如 `1,2,3` |

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

## 实现来源文件

- `/Users/wuyang/Documents/site/zhiyujia/ruoyi-vue-wxmini/.worktrees/merchant-user-type-whitelist-backend/ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/MerchantUserTypeWhitelistController.java`
- `/Users/wuyang/Documents/site/zhiyujia/ruoyi-vue-wxmini/.worktrees/merchant-user-type-whitelist-backend/ruoyi-system/src/main/resources/mapper/system/MerchantUserTypeWhitelistMapper.xml`
