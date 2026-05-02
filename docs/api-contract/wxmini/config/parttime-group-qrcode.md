# GET /wxmini/config/parttime-group-qrcode

## 用途

- `GET`：获取兼职群二维码和说明文案。

## 鉴权

- 需要登录。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "title": "兼职群二维码",
    "qrcodeUrl": "https://example.com/qrcode.png",
    "description": "扫码加入兼职通知群",
    "buttonText": "我知道了"
  }
}
```

## 失败场景或特殊说明

- 未配置二维码地址时返回业务错误，不返回空字符串伪成功。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxConfigController.java`
