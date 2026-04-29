# GET /wxmini/address/list

## 用途

- `GET`：查询当前登录用户的服务地址列表。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

## Path 参数

- 无。

## GET 请求

### Query 参数

- 无。

### Body 示例

- 无。

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "id": 1924135123456789012,
      "userId": "wx-user-123",
      "contactName": "王女士",
      "contactPhone": "13800138000",
      "region": "江苏省 南京市 鼓楼区",
      "location": "鼓楼区xx小区",
      "geo": "118.7901,32.0601",
      "addressDetail": "3栋",
      "doorplate": "1201",
      "isDefault": 1,
      "remark": "进门前电话联系",
      "createTime": "2026-04-29 18:00:00",
      "updateTime": "2026-04-29 18:00:00"
    }
  ]
}
```

### 失败场景或特殊说明

- 未登录：`msg = 请先登录`。
- 列表按 `is_default desc, update_time desc, id desc` 排序。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxAddressController.java`
