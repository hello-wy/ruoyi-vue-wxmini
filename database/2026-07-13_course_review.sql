-- 课程评价表
-- 执行环境：生产 zhiyujia
-- 说明：支持已报名用户按订单发布/更新课程评价，并按课程公开查询。

CREATE TABLE IF NOT EXISTS `course_review` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(64) NOT NULL COMMENT '课程报名订单号',
  `user_id` varchar(64) NOT NULL COMMENT '小程序用户 UUID',
  `course_id` bigint NOT NULL COMMENT '课程 ID',
  `content` varchar(1000) NOT NULL COMMENT '评价内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_course_review_order_no` (`order_no`),
  KEY `idx_course_review_course_time` (`course_id`, `update_time`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='课程评价表';
