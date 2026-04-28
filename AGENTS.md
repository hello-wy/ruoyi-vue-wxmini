# ruoyi-vue-wxmini AGENTS

## 1. 适用范围

本文件只描述 `ruoyi-vue-wxmini` 后端项目的模块职责、接口维护规则和交付要求。
前后端联调规则以仓库根目录 `AGENTS.md` 为准。

## 2. 模块职责

- `ruoyi-admin/`：后台启动模块与控制器
- `ruoyi-wxmini/`：小程序控制器、鉴权、微信能力
- `ruoyi-system/`：业务 domain、service、mapper
- `ruoyi-framework/`：安全、过滤器、配置
- `ruoyi-common/`：公共响应体、工具、异常
- `docs/api-contract/`：接口契约文档目录

## 3. 什么时候优先看接口文档

仅当任务涉及以下情况时，优先读取 `docs/api-contract/`：
- 新增接口
- 修改接口路径、参数、响应、鉴权
- 排查前后端接口不一致
- 需要确认真实契约

普通 service 逻辑修改、内部重构、非接口问题，按需看文档，不强制先读完整文档链。

## 4. 接口维护规则

- 新增接口时，同步创建对应文档
- 修改接口时，同步更新原文档
- 删除接口时，同步删除或标记废弃文档
- 改动以下内容时必须更新文档：
  - URL
  - Method
  - 鉴权方式
  - Query / Body 字段
  - 返回结构
  - 错误码或错误消息

## 5. 鉴权与响应

- 小程序前台普通用户接口使用 `Wx-Authorization`
- 管理后台与系统管理类接口使用 `Authorization`
- 对象结果优先保持 `{ code, msg, data }`
- 分页结果优先保持 `{ code, msg, total, rows }`
- 历史非标准结构必须在文档中显式说明

## 6. 完成标准

一个可交付的接口类改动，至少满足：
- 相关实现已完成
- `docs/api-contract/` 已同步更新
- 文档中的鉴权、参数、响应示例与当前代码一致
