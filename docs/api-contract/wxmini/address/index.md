# POST|PUT /wxmini/address

## 用途

- `POST`：新增服务地址。
- `PUT /wxmini/address/{id}`：修改服务地址。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

## Path 参数

- `PUT` 时：`id` 为服务地址 ID。
- `POST` 时：无。

## POST 请求

### Query 参数

- 无。

### Body 示例

```json
{
  "contactName": "王女士",
  "contactPhone": "13800138000",
  "region": "江苏省 南京市 鼓楼区",
  "location": "鼓楼区xx小区",
  "geo": "118.7901,32.0601",
  "addressDetail": "3栋",
  "doorplate": "1201",
  "isDefault": 1,
  "remark": "进门前电话联系"
}
```

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": 1924135123456789012
}
```

### 失败场景或特殊说明

- 未登录：`msg = 请先登录`。
- 参数错误：`msg = 参数错误`。
- 联系人为空：`msg = 请填写联系人`。
- 联系电话非法：`msg = 请填写正确的联系电话`。
- 省市区为空：`msg = 请选择省市区`。
- 详细地址为空：`msg = 请选择详细地址`。
- 新增时由后端生成雪花 ID 并写入当前用户 `userId`。
- 若当前用户尚无任何地址，service 层会将首条地址自动设为默认地址。
- 若 `isDefault = 1`，service 层会清空当前用户其他默认地址。

## PUT 请求

### Query 参数

- 无。

### Body 示例

```json
{
  "contactName": "王女士",
  "contactPhone": "13800138000",
  "region": "江苏省 南京市 鼓楼区",
  "location": "鼓楼区yy小区",
  "geo": "118.7911,32.0611",
  "addressDetail": "5栋",
  "doorplate": "802",
  "isDefault": 0,
  "remark": "工作日晚上可上门"
}
```

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 失败场景或特殊说明

- 同 `POST` 参数校验。
- 地址不存在或不属于当前用户：`msg = 地址不存在或无权修改`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxAddressController.java`
