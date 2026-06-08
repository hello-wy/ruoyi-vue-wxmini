ALTER TABLE lectures
    ADD COLUMN registration_fee DECIMAL(10, 2) NOT NULL DEFAULT 100.00 COMMENT '报名费';

CREATE TABLE course_pay_order (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    order_no VARCHAR(64) NOT NULL COMMENT '平台订单号',
    user_id VARCHAR(64) NOT NULL COMMENT '小程序用户UUID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    name VARCHAR(64) DEFAULT NULL COMMENT '报名姓名',
    gender VARCHAR(16) DEFAULT NULL COMMENT '性别',
    phone VARCHAR(32) DEFAULT NULL COMMENT '联系电话',
    company VARCHAR(128) DEFAULT NULL COMMENT '单位',
    accommodation VARCHAR(64) DEFAULT NULL COMMENT '住宿需求',
    enrollment_id BIGINT DEFAULT NULL COMMENT '学籍/报名关联ID',
    amount DECIMAL(10, 2) NOT NULL COMMENT '支付金额',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '0待支付 1已支付待签到 2已签到 3已退款 4已取消',
    wechat_transaction_id VARCHAR(64) DEFAULT NULL COMMENT '微信支付交易号',
    refund_no VARCHAR(64) DEFAULT NULL COMMENT '退款单号',
    pay_time DATETIME DEFAULT NULL COMMENT '支付时间',
    sign_time DATETIME DEFAULT NULL COMMENT '签到时间',
    refund_time DATETIME DEFAULT NULL COMMENT '退款时间',
    request_id VARCHAR(64) DEFAULT NULL COMMENT '微信回调Request-ID',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_course_pay_order_no (order_no),
    KEY idx_course_user_status (course_id, user_id, status),
    KEY idx_user_create_time (user_id, create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程报名支付订单';
