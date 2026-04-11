# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a RuoYi-based WeChat mini-program backend system that integrates the standard RuoYi admin framework with WeChat mini-program functionality. The project consists of a Java Spring Boot backend with a Vue.js admin UI.

## Development Commands

### Backend (Java/Maven)

```bash
# Build the entire project (from root directory)
mvn clean package

# Skip tests during build
mvn clean package -DskipTests

# Run the backend server (requires Java 8, Redis, MySQL)
java -jar ruoyi-admin/target/ruoyi-admin.jar

# Run with specific profile
java -jar ruoyi-admin/target/ruoyi-admin.jar --spring.profiles.active=dev

# Run from target directory after build
cd ruoyi-admin/target && java -jar ruoyi-admin.jar
```

### Frontend (Vue.js Admin UI)

```bash
cd ruoyi-ui

# Install dependencies
npm install

# Development server
npm run dev

# Build for production
npm run build:prod

# Build for staging
npm run build:stage

# Lint code
npm run lint
```

## Architecture

### Multi-Module Maven Structure

The project is organized as a multi-module Maven project:

- **ruoyi-admin** - Web service entry point, contains REST controllers and main application class (`RuoYiApplication`)
- **ruoyi-framework** - Core framework configuration (Spring Security, filters, interceptors, MyBatis-Plus)
- **ruoyi-system** - System management (users, roles, permissions, dictionaries, dept, menu)
- **ruoyi-common** - Shared utilities, annotations, exceptions, domain objects
- **ruoyi-quartz** - Scheduled task management
- **ruoyi-generator** - Code generation tools
- **ruoyi-wxmini** - WeChat mini-program specific functionality
- **ruoyi-ui** - Vue.js admin frontend

### Dual Authentication System

The project implements **two independent authentication systems**:

1. **Admin Authentication** (RuoYi default)
   - Header: `Authorization`
   - Used for: Management backend (`/system/*`, `/monitor/*`, `/tool/*`)
   - Implementation: Spring Security in `ruoyi-framework/config/SecurityConfig`
   - Token management: `com.ruoyi.framework.web.service.TokenService`

2. **WeChat Mini-Program Authentication** (Custom)
   - Header: `Wx-Authorization`
   - Format: `Bearer {JWT_TOKEN}`
   - Used for: Mini-program APIs (`/wxmini/**`)
   - Implementation: `ruoyi-wxmini/filter/WxMiniJwtFilter`
   - User context: `WxMiniUserContext.getCurrentUserId()` (ThreadLocal-based)
   - JWT Service: `IWxMiniJwtService.createToken(userId)`

The two systems are completely independent - WeChat users cannot access the admin backend and vice versa.

### WeChat Mini-Module (`ruoyi-wxmini`)

This module contains WeChat-specific functionality:

**Key Components:**
- `WxMiniJwtFilter` - JWT authentication filter for mini-program requests
  - Intercepts all `/wxmini/**` requests
  - Validates `Wx-Authorization` header
  - Manages ThreadLocal user context

- `WxMiniUserContext` - ThreadLocal utility for accessing current WeChat user ID
  - `getCurrentUserId()` - Get authenticated user ID
  - Automatically cleaned up in filter's finally block

- `AbsWxPayBaseService<P>` - Template method pattern for payment processing
  - Generic parameter type `<P>` for different payment scenarios
  - Lock-free concurrency control using ConcurrentHashMap
  - Payment flow: resource locking → validation → param building → order creation → persistence

**Package Structure:**
- `controller/` - REST controllers for WeChat APIs (login, pay, growup, tutoring, etc.)
- `service/` - Business logic services
- `filter/` - Authentication filters
- `config/` - WeChat and Aliyun configuration
- `util/` - Utility classes (user context, etc.)
- `vo/` - View objects for API responses
- `bo/` - Business objects for requests

### Spring Security Configuration

Located in `ruoyi-framework/config/SecurityConfig`:
- `/wxmini/**` endpoints are permitted (bypass Spring Security, handled by custom filter)
- Admin endpoints require standard authentication
- CORS and CSRF configurations

### Database Layer

- **ORM**: MyBatis-Plus 3.5.3 (primary) + PageHelper (legacy)
- **Connection Pool**: Alibaba Druid
- **Mapper Locations**: `classpath*:mapper/**/*Mapper.xml`
- **Type Aliases**: `com.ruoyi.**.domain`

### Configuration Files

- Main config: `ruoyi-admin/src/main/resources/application.yml`
- Profiles: `application-dev.yml`, `application-prod.yml`
- WeChat config: `wx.miniapp` and `wx.pay` sections in application.yml
- MyBatis config: `mybatis/mybatis-config.xml`

## Runtime Requirements

- **Java**: JDK 8
- **Database**: MySQL
- **Cache**: Redis (required for session management)
- **Build Tool**: Maven 3.x
- **Frontend**: Node.js >= 8.9, npm >= 3.0.0

## WeChat Integration

The project integrates with WeChat using the WxJava library (binarywang/WxJava):

- **Mini-program Login**: User authentication via WeChat OAuth
- **Payment**: WeChat Pay V3 integration
- **User Management**: Separate `wx_user` table for mini-program users
- **Configuration**: AppID and secret configured in `application.yml`

## API Endpoints

- **Admin APIs**: `/system/*`, `/monitor/*`, `/tool/*` (require `Authorization` header)
- **WeChat APIs**: `/wxmini/*` (require `Wx-Authorization` header)
- **Swagger**: `/swagger-ui/` (when enabled in config)

## Frontend Architecture (Vue Admin)

- **Framework**: Vue 2.6.12 + Element UI 2.15.14
- **State Management**: Vuex
- **Routing**: Vue Router
- **HTTP Client**: Axios
- **Build Tool**: Vue CLI 4.4.6
- **Entry Point**: `ruoyi-ui/src/main.js`
- **Router**: `ruoyi-ui/src/router/index.js`
- **API Calls**: `ruoyi-ui/src/api/`

## Key Dependencies

- Spring Boot 2.5.15
- Spring Security 5.7.12
- MyBatis-Plus 3.5.3
- Druid 1.2.23
- Redis (Lettuce client)
- JWT (jjwt 0.9.1)
- Swagger 3.0.0
- WxJava (WeChat SDK)

## Code Generation

The project includes a code generator at `/tool/gen` (when running) that can generate:
- Domain entities
- Mapper interfaces and XML
- Service interfaces and implementations
- Controllers
- Vue frontend pages

Generated business code for wxmini entities is placed in `ruoyi-system` module under `com.ruoyi.wxmini` package.
