ALTER TABLE lectures
    ADD COLUMN is_top TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否在首页最近课程区域置顶展示: 1是, 0否';
