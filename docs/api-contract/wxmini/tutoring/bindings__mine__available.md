# GET /wxmini/tutoring/bindings/mine/available

## 用途

- `GET`：查询当前登录家长仍可下单的家教绑定列表。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

## Query 参数

- 无。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "id": 20001,
      "parentId": 30001,
      "parentUserId": 101,
      "tutorId": 40001,
      "tutorUserId": 202,
      "status": 0,
      "serviceTimesSnapshot": "[{\"serviceDate\":\"2026-05-20\",\"startTime\":\"18:00\",\"endTime\":\"20:00\"}]",
      "parentName": "周内晚间数学作业辅导",
      "parentSubject": "数学",
      "parentGrade": "初一",
      "parentRegion": "南京市江宁区",
      "tutorName": "王教员",
      "tutorSchool": "南京大学",
      "tutorCurrentGrade": "大三"
    }
  ]
}
```

## 失败场景或特殊说明

- 未登录：`msg = 请先登录`。
- 当前实现按平台用户 `parentUserId` 查询，仅返回 `status = 0` 的绑定关系。
- 绑定中的 `parent_wechat_uid` 不再冗余存储，家长侧统一依赖 `parentUserId`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxTutoringPayServiceImpl.java`
