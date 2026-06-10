ALTER TABLE lectures
    ADD COLUMN enrolled_count INT NOT NULL DEFAULT 0 COMMENT '已报名人数';
