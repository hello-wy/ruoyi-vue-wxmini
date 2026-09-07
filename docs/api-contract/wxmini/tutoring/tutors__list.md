# GET /wxmini/tutoring/tutors/list

## 用途

- 分页查询已审核通过的公开教员列表。

## 鉴权

- 公开接口。

## Query 参数

- `pageNum`：页码，默认 `1`。
- `pageSize`：每页数量，默认 `5`。
- `subject`：科目模糊筛选，可选。
- `region`：区域模糊筛选，可选。
- `methods`：授课方式精确筛选，可选。
- `grade`：学历精确筛选，可选。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 1,
  "rows": [
    {
      "id": "1234567890123456700",
      "identity": 0,
      "status": 1,
      "realName": "张老师",
      "school": "南京大学",
      "major": "数学与应用数学",
      "subjects": "8,10",
      "areas": "320115,320114",
      "methods": 1,
      "degree": 1,
      "city": "江宁区",
      "certificateList": "教师资格证,英语六级"
    }
  ]
}
```

## 隐私说明

- 公开列表不返回 `certificates` 和 `materials`。
- 身份证正反面、学生证和证书图片仅允许本人接口与后台审核接口读取。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/TutorsServiceImpl.java`
