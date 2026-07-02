-- 学员回访记录表
-- 执行环境：dev_zyj
-- 说明：用于 pages/mine/admin/student/detail 学员回访页，支持新增回访记录和按学员查看回访表单记录。

CREATE TABLE IF NOT EXISTS `student_follow_up_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` bigint NOT NULL COMMENT '学员ID，对应 user_info.id',
  `follow_up_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '回访时间',
  `follow_up_method` tinyint NOT NULL DEFAULT '0' COMMENT '回访方式：0未指定，1电话，2微信，3线下，4其他',
  `follow_up_result` tinyint NOT NULL DEFAULT '0' COMMENT '回访结果：0未记录，1已接通，2未接通，3需再次跟进，4已完成',
  `content` text COMMENT '回访内容',
  `form_data` json DEFAULT NULL COMMENT '回访表单扩展数据(JSON)',
  `next_follow_up_time` datetime DEFAULT NULL COMMENT '下次回访时间',
  `operator_id` bigint DEFAULT NULL COMMENT '回访人ID，对应 sys_user.user_id',
  `operator_name` varchar(64) DEFAULT NULL COMMENT '回访人姓名快照',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除标识：0正常，1删除',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_follow_up_student_time` (`student_id`, `is_deleted`, `follow_up_time`),
  KEY `idx_student_follow_up_next_time` (`next_follow_up_time`, `is_deleted`),
  KEY `idx_student_follow_up_operator` (`operator_id`, `follow_up_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学员回访记录表';
