# 本地接口文档说明

## 1. 目的

`docs/api-contract/` 是当前仓库内给 agent 和开发者共用的本地接口文档目录。前端联调、小程序开发、接口改造时，先读这里，再读 controller。

## 2. 路径映射

- `/wxmini/growup/courses` -> `wxmini/growup/courses.md`
- `/wxmini/growup/courses/{id}` -> `wxmini/growup/courses__{id}.md`
- `/wxmini/pay/order/create` -> `wxmini/pay/order__create.md`
- `/system/wallet/info` -> `system/wallet/info.md`
- `/list` -> `_root/list.md`

规则：

- 最多保留两层目录。
- 第三层及以后路径片段用 `__` 拼进文件名。
- 同一路径支持多种 HTTP Method 时，写在同一个文件里。

## 3. 读取顺序

1. 按目标 URL 找到文档。
2. 读取鉴权、参数、返回示例。
3. 文档缺失或不完整时，再回看 controller。
4. 补齐文档后再继续开发。

## 4. 当前已落地文档

- `wxmini/login.md`
- `wxmini/user/info.md`
- `wxmini/user/phone.md`
- `wxmini/growup/courses.md`
- `wxmini/growup/courses__{id}.md`
- `wxmini/growup/enrollments__list.md`
- `wxmini/growup/enrollments__total.md`
- `wxmini/faceAuth/init.md`
- `wxmini/faceAuth/query.md`
- `wxmini/pay/order__create.md`
- `wxmini/pay/order__query.md`
- `wxmini/pay/notify.md`
- `wxmini/tutoring/tutors__list.md`
- `wxmini/tutoring/apply.md`
- `wxmini/tutoring/review.md`
- `wxmini/tutoring/mine.md`
- `wxmini/tutoring/parents__list.md`
- `wxmini/tutoring/parents__mine.md`
- `wxmini/portal/appid.md`
- `system/wallet/info.md`
- `system/wallet/withdraw.md`
- `system/wallet/withdrawRecords.md`
- `_root/list.md`
