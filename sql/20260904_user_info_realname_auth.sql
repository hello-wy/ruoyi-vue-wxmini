-- 实名认证接口依赖字段：历史数据库未同步字段时，会导致认证成功后写库失败。
ALTER TABLE user_info
    ADD COLUMN real_name varchar(50) DEFAULT NULL COMMENT '实名认证真实姓名' AFTER avatar_url,
    ADD COLUMN id_card varchar(18) DEFAULT NULL COMMENT '实名认证身份证号码' AFTER real_name,
    ADD COLUMN is_realname_auth tinyint NOT NULL DEFAULT 0 COMMENT '是否已实名认证：0-未认证，1-已认证' AFTER id_card;
