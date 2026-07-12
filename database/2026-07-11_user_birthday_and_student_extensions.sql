-- 用户生日与学员回访标题
-- 执行环境：dev_zyj

ALTER TABLE `user_info`
    ADD COLUMN `birthday` date DEFAULT NULL COMMENT '用户生日' AFTER `phone`;

ALTER TABLE `student_follow_up_record`
    ADD COLUMN `title` varchar(200) NOT NULL DEFAULT '' COMMENT '回访标题' AFTER `follow_up_result`;
