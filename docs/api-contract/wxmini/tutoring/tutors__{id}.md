# GET /wxmini/tutoring/tutors/{id}

## 用途

- 获取已审核通过的公开教员详情。

## 鉴权

- 公开接口。

## 请求头

- 无。

## Path 参数

- `id`：教员 ID。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": "1234567890123456700",
    "realName": "张老师",
    "identity": 0,
    "subjects": "8,10",
    "areas": "320115,320114",
    "methods": 1,
    "status": 1,
    "experience": "3年家教经验\n擅长提分",
    "major": "数学与应用数学",
    "school": "南京大学",
    "degree": 1,
    "currentGrade": "大三",
    "selfJudge": "认真负责，善于沟通",
    "certificateList": "教师资格证,英语六级",
    "city": "江宁区"
  }
}
```

## 隐私说明

- 公开详情不返回 `certificates` 和 `materials`。
- 身份证正反面、学生证和证书图片仅允许本人接口与后台审核接口读取。

## 失败场景或特殊说明

- 教员不存在或未通过审核：`msg = 教员不存在`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/vo/WxTutorDetailVo.java`
