# GET /wxmini/tutoring/parents/list

## 用途

- `GET`：分页查询有效家教单列表（公开，支持科目/区域/辅导方式/年级筛选）。

## 鉴权

- 公开接口。

## 请求头

- 无。

## Path 参数

- 无。

## GET 请求

### Query 参数

- `pageNum`：若依标准分页页码，默认 `1`。
- `pageSize`：若依标准分页每页条数，默认 `10`。
- `subject`：`String`，科目筛选，模糊匹配。
- `region`：`String`，区域筛选，模糊匹配。
- `methods`：`Long`，辅导方式筛选，精确匹配。
- `grade`：`String`，年级筛选，模糊匹配。

### Body 示例

- 无。

### 成功响应示例

```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 1,
  "rows": [
    {
      "id": 1234567890123456789,
      "addressId": 1924135123456789012,
      "babyId": 1924135000000000001,
      "name": "周内晚间数学作业辅导",
      "phone": "13800138000",
      "grade": "初一",
      "subject": "数学",
      "methods": 1,
      "demandItems": "1,2,4",
      "serviceDates": "2026-05-01,2026-05-03",
      "serviceTimes": "[{\"startTime\":\"18:00\",\"endTime\":\"20:00\"}]",
      "dayOfWeek": "5,7",
      "startTime": "18:00",
      "endTime": "20:00",
      "region": "江苏省 南京市 鼓楼区",
      "location": "鼓楼区xx小区 3栋 1201",
      "geo": "118.7901,32.0601",
      "brief": "孩子目前基础一般，需要重点补作业和错题。",
      "requirements": "女教员优先，沟通耐心。",
      "status": 0,
      "createDate": "2026-05-01",
      "updateDate": "2026-05-01"
    }
  ]
}
```

### 失败场景或特殊说明

- 仅返回有效家教单列表，当前实现调用 `parentsService.selectActiveParentsList(filter)`。
- `rows` 中实际返回字段以 `Parents` 实体与 mapper 查询结果为准；当前已包含 `addressId`、`phone`、`serviceDates`、`serviceTimes`、`demandItems` 等新字段。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
