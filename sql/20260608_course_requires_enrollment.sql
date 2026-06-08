ALTER TABLE lectures
    ADD COLUMN requires_enrollment TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否需要学籍: 1需要, 0不需要';

UPDATE lectures
SET requires_enrollment = 0;
