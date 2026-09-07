-- 教员审核材料多图存储：certificates 改为材料 ID 列表，图片 URL 下沉到材料表。
ALTER TABLE tutors
    MODIFY COLUMN certificates TEXT DEFAULT NULL
    COMMENT '审核材料ID列表，逗号分隔';

CREATE TABLE IF NOT EXISTS tutor_material (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '材料ID',
    tutor_id BIGINT DEFAULT NULL COMMENT '教员ID',
    owner_uid VARCHAR(64) NOT NULL COMMENT '材料所属小程序用户ID',
    type TINYINT NOT NULL COMMENT '材料类型：1身份证正面 2身份证反面 3学生证 4证书',
    url VARCHAR(512) NOT NULL COMMENT '图片URL',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_tutor_material_tutor (tutor_id),
    KEY idx_tutor_material_owner (owner_uid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教员审核材料';

-- 将旧版单张证书 URL 迁移为 type=4 的材料，并回写材料 ID。
INSERT INTO tutor_material (tutor_id, owner_uid, type, url)
SELECT id, uid, 4, certificates
FROM tutors
WHERE certificates IS NOT NULL
  AND certificates <> ''
  AND (
      certificates LIKE 'http://%'
      OR certificates LIKE 'https://%'
      OR certificates LIKE '/%'
  )
  AND NOT EXISTS (
      SELECT 1
      FROM tutor_material AS existing_material
      WHERE existing_material.tutor_id = tutors.id
        AND existing_material.type = 4
        AND existing_material.url = tutors.certificates
  );

UPDATE tutors AS tutors_record
INNER JOIN tutor_material AS material
    ON material.tutor_id = tutors_record.id
    AND material.type = 4
    AND material.url = tutors_record.certificates
SET tutors_record.certificates = CAST(material.id AS CHAR)
WHERE tutors_record.certificates IS NOT NULL
  AND tutors_record.certificates <> ''
  AND (
      tutors_record.certificates LIKE 'http://%'
      OR tutors_record.certificates LIKE 'https://%'
      OR tutors_record.certificates LIKE '/%'
  );
