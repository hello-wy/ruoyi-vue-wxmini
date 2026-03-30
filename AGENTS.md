# ruoyi-vue-wxmini AGENTS

## 1. 项目定位

这是当前仓库中的 Spring Boot 后端子项目，基于 `RuoYi-Vue 3.8.9 + Spring Boot 2.5.15 + ruoyi-wxmini`。

本文件只描述后端项目自身的模块职责、接口文档维护方式和高可用要求。前后端如何联调，统一以仓库根目录 `AGENTS.md` 为准。

## 2. 模块职责

- `ruoyi-admin/`：管理后台启动模块、后台控制器、Swagger 配置。
- `ruoyi-wxmini/`：小程序侧控制器、鉴权、微信能力、人脸认证、支付能力。
- `ruoyi-system/`：主要业务 domain、service、mapper。
- `ruoyi-framework/`：安全、过滤器、配置。
- `ruoyi-common/`：公共响应体、工具、常量、异常。
- `sql/`：数据库脚本。
- `docs/api-contract/`：接口契约文档目录，是后端对外协作的第一输出。

## 3. 后端高可用原则

- 接口路径、字段语义、响应结构和鉴权规则应保持稳定，不做无说明变更。
- 历史接口若存在特殊行为，必须明确记录，不做主观纠正。
- 返回结构优先沿用若依标准，避免同类接口出现多套返回风格。
- 异常和失败场景应能被前端明确识别，禁止用沉默兼容掩盖契约问题。
- 任何会影响前端接入的变更，都必须可追踪、可文档化、可复核。

## 4. 接口文档规则

### 文档优先级

排查和维护接口时，优先读取：

1. `docs/api-contract/` 对应路径文档
2. controller 上的 `@RequestMapping`、`@GetMapping`、`@PostMapping`
3. Swagger 注解
4. 请求体 BO/DTO、返回 VO/domain
5. service 实现

### 存储规则

- 接口文档根目录固定为 `docs/api-contract/`。
- URL 只保留去掉首个 `/` 之后的 path 片段。
- 最多只分两层目录，第三层开始并入文件名。
- 文件扩展名固定为 `.md`。

示例：

- `/wxmini/growup/courses` -> `docs/api-contract/wxmini/growup/courses.md`
- `/wxmini/growup/courses/{id}` -> `docs/api-contract/wxmini/growup/courses__{id}.md`
- `/wxmini/pay/order/create` -> `docs/api-contract/wxmini/pay/order__create.md`
- `/system/wallet/withdraw` -> `docs/api-contract/system/wallet/withdraw.md`

### 文档内容

每个接口文档至少包含：

- 标题：`METHOD PATH`
- 用途说明
- 鉴权要求
- 请求头
- Path 参数
- Query 参数
- Body 示例
- 成功响应示例
- 失败场景或特殊说明
- 实现来源文件

## 5. 接口维护规则

- 新增接口时，先确定最终 URL，再同步创建文档文件。
- 修改接口时，同步更新原文档，不新建重复文档。
- 删除接口时，同步删除或标记废弃文档。
- 改动以下任一内容都必须更新文档：
  - URL
  - Method
  - 鉴权方式
  - Query / Body 字段
  - 返回结构
  - 错误码或错误消息

## 6. 鉴权与响应

- `/wxmini/**` 默认走 `Wx-Authorization: Bearer <token>`。
- 当前公开白名单以 `ruoyi-wxmini` 中实际过滤器实现为准。
- 后台接口默认走若依标准 `Authorization`。
- 对象结果优先保持 `{ code, msg, data }`。
- 分页结果优先保持 `{ code, msg, total, rows }`。
- 若存在历史非标准结构，必须在文档中显式写明。

## 7. 后端修改完成标准

一个可交付的接口变更，至少满足以下条件：

- controller 或相关实现已完成。
- `docs/api-contract/` 对应文档已更新。
- 文档中的鉴权、参数、响应示例与当前代码一致。
- 新加入的协作者只看本文档和接口文档，就能理解当前真实契约。
