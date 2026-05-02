# GET /wxmini/config/merchant-agent

## 用途

- `GET`：获取商家端“成为代理”弹窗配置。

## 鉴权

- 需要登录。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "title": "成为代理",
    "content": "联系平台获取代理合作详情。",
    "qrcodeUrl": "https://example.com/merchant-agent.png",
    "contact": "wechat123",
    "tips": "添加时请备注代理合作",
    "buttonText": "我知道了"
  }
}
```

## 失败场景或特殊说明

- 当内容、二维码、联系方式均未配置时返回业务错误。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxConfigController.java`
