# GET /wxmini/growup/courses/{id}

## 用途

获取课程或讲座详情。

## 鉴权

- 公开接口

## Path 参数

- `id`：必填，课程或讲座 ID

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "title": "课程标题"
  }
}
```

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxGrowupController.java`
