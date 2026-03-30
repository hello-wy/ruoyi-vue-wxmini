# GET|POST /wxmini/portal/{appid}

## 用途

微信服务器接入校验和消息推送入口，不属于前端主动调用接口。

## 鉴权

- 公开接口
- 由微信服务器调用

## GET 用途

- 校验接入合法性，成功时原样返回 `echostr`

## POST 用途

- 接收微信服务器推送消息或事件
- 成功时返回 `success`

## Path 参数

- `appid`：必填，小程序 AppID

## 特殊说明

- 这是服务端回调入口，前端页面不应直接接入。

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxPortalController.java`
