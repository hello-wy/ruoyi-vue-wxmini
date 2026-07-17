# CLAUDE.md

## Project Overview

`ruoyi-vue-wxmini` 是智育傢的 Spring Boot 后端子项目，包含若依后台能力和小程序业务能力。

## Work In This Project

- 后端任务优先看 `ruoyi-vue-wxmini/AGENTS.md`
- 接口类任务再结合 `docs/api-contract/` 和仓库根目录 `AGENTS.md`
- 普通内部实现、修 bug、重构，不必默认先读完整接口文档链

## Key Modules

- `ruoyi-admin/`：启动与后台控制器
- `ruoyi-framework/`：安全、过滤器、配置
- `ruoyi-system/`：主要业务 domain / service / mapper
- `ruoyi-common/`：公共能力
- `ruoyi-wxmini/`：小程序接口、鉴权、微信相关能力
- `ruoyi-ui/`：Vue 管理端

## Auth

- 管理后台默认使用 `Authorization`
- 小程序接口默认使用 `Wx-Authorization`
- 小程序用户上下文通过 `WxMiniUserContext.getCurrentUserId()` 获取

## Common Rules
- 接口类任务优先核对 `docs/api-contract/`
- ORM 主要为 MyBatis-Plus + MyBatis XML
- 配置入口优先查看 `ruoyi-admin/src/main/resources/application*.yml`
