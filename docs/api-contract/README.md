# 本地接口文档说明

## 1. 目的

`docs/api-contract/` 是当前仓库内给开发者和 agent 共用的后端接口契约目录。联调、补文档、核对真实路径时，先读这里，再读 controller。

## 2. 目录约束

- 业务接口文档只放在 `system/` 和 `wxmini/` 两个目录下。
- 根目录仅保留本说明文件。
- 当某个资源既有根路径（例如 `/system/jobs`）又有更深层子路径（例如 `/system/jobs/list`）时，资源根路径文档使用 `index.md`。
- URL 第三段及以后需要收敛到同一个文件名时，使用 `__` 连接，例如：`/wxmini/pay/jobs/orders/create -> wxmini/pay/jobs__orders__create.md`。
- 同一路径支持多种 HTTP Method 时，写在同一个文件里。

## 3. 读取顺序

1. 先按真实 URL 定位到对应文档。
2. 再看鉴权、参数、成功示例和特殊说明。
3. 文档缺失或怀疑过期时，再回看 controller / BO / VO / service。

## 4. 当前已落地文档

- `system/auth/export.md`
- `system/auth/index.md`
- `system/auth/list.md`
- `system/auth/{ids}.md`
- `system/auth/{id}.md`
- `system/course-cashback.md`
- `system/enrollment/export.md`
- `system/enrollment/index.md`
- `system/enrollment/list.md`
- `system/enrollment/{ids}.md`
- `system/enrollment/{id}.md`
- `system/info/export.md`
- `system/info/index.md`
- `system/info/list.md`
- `system/info/{ids}.md`
- `system/info/{id}.md`
- `system/job-sign-audit__list.md`
- `system/job-sign-audit__refund-check__{orderNo}.md`
- `system/job-sign-audit__{id}__audit.md`
- `system/jobs/export.md`
- `system/jobs/index.md`
- `system/jobs/list.md`
- `system/jobs/{ids}.md`
- `system/jobs/{id}.md`
- `system/lectures/export.md`
- `system/lectures/index.md`
- `system/lectures/list.md`
- `system/lectures/recent.md`
- `system/lectures/{ids}.md`
- `system/lectures/{id}.md`
- `system/material/export.md`
- `system/material/index.md`
- `system/material/list.md`
- `system/material/upload.md`
- `system/material/{ids}.md`
- `system/material/{id}.md`
- `system/parents/export.md`
- `system/parents/index.md`
- `system/parents/list.md`
- `system/parents/{ids}.md`
- `system/parents/{id}.md`
- `system/profile/export.md`
- `system/profile/index.md`
- `system/profile/list.md`
- `system/profile/{ids}.md`
- `system/profile/{id}.md`
- `system/refund/job__list.md`
- `system/refund/job__{orderNo}.md`
- `system/refund/salon__list.md`
- `system/refund/salon__{orderNo}.md`
- `system/questionnaire/export.md`
- `system/questionnaire/getQuestionnaire__{lecturesId}.md`
- `system/questionnaire/index.md`
- `system/questionnaire/list.md`
- `system/questionnaire/{ids}.md`
- `system/questionnaire/{id}.md`
- `system/record/export.md`
- `system/record/index.md`
- `system/record/list.md`
- `system/record/myRecords.md`
- `system/record/{ids}.md`
- `system/record/{id}.md`
- `system/student/list.md`
- `system/student/{id}.md`
- `system/tradeOrder/export.md`
- `system/tradeOrder/index.md`
- `system/tradeOrder/list.md`
- `system/tradeOrder/{ids}.md`
- `system/tradeOrder/{id}.md`
- `system/tutoring-admin__bindings.md`
- `system/tutoring-admin__bindings__list.md`
- `system/tutoring-admin__orders.md`
- `system/tutoring-admin__payroll__batch-pay.md`
- `system/tutoring-admin__payroll__list.md`
- `system/tutoring-admin__schedules__list.md`
- `system/tutoring-admin__schedules__{id}__audit.md`
- `system/tutors/export.md`
- `system/tutors/index.md`
- `system/tutors/list.md`
- `system/tutors/review.md`
- `system/tutors/{ids}.md`
- `system/tutors/{id}.md`
- `system/wallet/info.md`
- `system/wallet/withdraw.md`
- `system/wallet/withdrawRecords.md`
- `wxmini/baby/index.md`
- `wxmini/baby/list.md`
- `wxmini/baby/{id}.md`
- `wxmini/common__uploadJobSignImage.md`
- `wxmini/growup/courses.md`
- `wxmini/growup/courses__{id}.md`
- `wxmini/growup/courses__{id}__enroll.md`
- `wxmini/growup/courses__{id}__reviews.md`
- `wxmini/growup/courses__{id}__reviews__my.md`
- `wxmini/growup/enrollments__list.md`
- `wxmini/growup/enrollments__total.md`
- `wxmini/growup/notes.md`
- `wxmini/growup/materials.md`
- `wxmini/growup/materials__files__auth.md`
- `wxmini/growup/materials__files__{uuid}.md`
- `wxmini/jobs/index.md`
- `wxmini/jobs/list.md`
- `wxmini/jobs/mine__defaults.md`
- `wxmini/jobs__schedules__my.md`
- `wxmini/jobs__{jobId}__sign-in.md`
- `wxmini/jobs/{jobId}__signup-users.md`
- `wxmini/login.md`
- `wxmini/pay/jobs__notify.md`
- `wxmini/pay/jobs__orders__create.md`
- `wxmini/pay/jobs__orders__my.md`
- `wxmini/pay/jobs__orders__{orderNo}.md`
- `wxmini/pay/courses__notify.md`
- `wxmini/pay/courses__orders__create.md`
- `wxmini/pay/courses__orders__my.md`
- `wxmini/pay/courses__orders__paid.md`
- `wxmini/pay/courses__orders__{orderNo}.md`
- `wxmini/pay/courses__orders__{orderNo}__cancel.md`
- `wxmini/pay/growup__notify.md`
- `wxmini/pay/salon__notify.md`
- `wxmini/pay/tutoring__notify.md`
- `wxmini/pay/salon__orders__create.md`
- `wxmini/pay/salon__orders__my.md`
- `wxmini/pay/salon__orders__{orderNo}.md`
- `wxmini/portal/{appid}.md`
- `wxmini/profile/detail.md`
- `wxmini/profile/index.md`
- `wxmini/profile/user-type.md`
- `wxmini/profile/user-type__init.md`
- `wxmini/tutoring/apply.md`
- `wxmini/tutoring/bindings__mine__available.md`
- `wxmini/tutoring/mine.md`
- `wxmini/tutoring/orders__create.md`
- `wxmini/tutoring/orders__my.md`
- `wxmini/tutoring/orders__{orderNo}.md`
- `wxmini/tutoring/orders__{orderNo}__pay.md`
- `wxmini/tutoring/parents.md`
- `wxmini/tutoring/parents__list.md`
- `wxmini/tutoring/parents__mine.md`
- `wxmini/tutoring/real-verify__verify.md`
- `wxmini/tutoring/schedules__my.md`
- `wxmini/tutoring/schedules__{id}__confirm.md`
- `wxmini/tutoring/schedules__{id}__finish.md`
- `wxmini/tutoring/tutors__list.md`
- `wxmini/tutoring/tutors__{id}.md`
- `wxmini/user/info.md`
- `wxmini/user/phone.md`
