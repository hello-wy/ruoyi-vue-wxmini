# GET /system/tutors/{id}

## 用途

- 获取后台教员审核详情，包括身份证、学生证和证书材料。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要权限标识：`system:tutors:list`。
- 此接口不是公开接口，禁止使用小程序普通用户 token 访问。

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- `id`：教员 ID。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": "1234567890123456700",
    "realName": "张三",
    "status": 0,
    "certificates": "1234567890123456789,1234567890123456790",
    "materials": [
      {
        "id": "1234567890123456789",
        "type": 1,
        "url": "/profile/upload/id-card-front.jpg"
      },
      {
        "id": "1234567890123456790",
        "type": 4,
        "url": "/profile/upload/certificate.jpg"
      }
    ]
  }
}
```

## 响应字段说明

- `certificates`：英文逗号分隔的材料 ID 列表。
- `materials`：按 ID 列表顺序返回材料详情。
- `materials[].type`：`1` 身份证正面、`2` 身份证反面、`3` 学生证、`4` 证书。

## 失败场景或特殊说明

- 未登录、token 无效或无 `system:tutors:list` 权限时，请求会被安全框架拦截。
- 教员不存在时，`data` 为 `null`。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/TutorsController.java`
