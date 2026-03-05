-- 首先是增加字典数据
    -- 科目
insert into sys_dict_type values(11,  '科目', 'sys_subject',       '0', 'admin', sysdate(), '', null, '科目列表');
insert into sys_dict_type values(12,  '辅导方式', 'sys_methods',       '0', 'admin', sysdate(), '', null, '辅导方式');
insert into sys_dict_type values(13,  '年级', 'sys_class',       '0', 'admin', sysdate(), '', null, '年级');
insert into sys_dict_type values(14,  '学历', 'sys_degree',       '0', 'admin', sysdate(), '', null, '学历');

-- 基础/阶段学科
insert into sys_dict_data values(30,  1,  '幼儿学前',   '0',  'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '幼儿学前');
insert into sys_dict_data values(31,  2,  '小学全科',   '1',  'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '小学全科');
insert into sys_dict_data values(32,  3,  '初中理科',   '2',  'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '初中理科');
insert into sys_dict_data values(33,  4,  '初中文科',   '3',  'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '初中文科');
insert into sys_dict_data values(34,  5,  '高中理科',   '4',  'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '高中理科');
insert into sys_dict_data values(35,  6,  '高中文科',   '5',  'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '高中文科');

-- 具体学科
insert into sys_dict_data values(36,  7,  '语文',       '6',  'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '语文');
insert into sys_dict_data values(37,  8,  '英语',       '7',  'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '英语');
insert into sys_dict_data values(38,  9,  '数学',       '8',  'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '数学');
insert into sys_dict_data values(39,  10, '奥数',       '9',  'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '奥数');
insert into sys_dict_data values(40,  11, '物理',       '10', 'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '物理');
insert into sys_dict_data values(41,  12, '化学',       '11', 'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '化学');
insert into sys_dict_data values(42,  13, '生物',       '12', 'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '生物');
insert into sys_dict_data values(43,  14, '历史',       '13', 'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '历史');
insert into sys_dict_data values(44,  15, '地理',       '14', 'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '地理');
insert into sys_dict_data values(45,  16, '政治',       '15', 'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '政治');

-- 艺术/体育/特长
insert into sys_dict_data values(46,  17, '钢琴',       '16', 'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '钢琴');
insert into sys_dict_data values(47,  18, '小提琴',     '17', 'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '小提琴');
insert into sys_dict_data values(48,  19, '古筝',       '18', 'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '古筝');
insert into sys_dict_data values(49,  20, '跳绳',       '19', 'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '跳绳');
insert into sys_dict_data values(50,  21, '篮球',       '20', 'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '篮球');
insert into sys_dict_data values(51,  22, '游泳',       '21', 'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '游泳');
insert into sys_dict_data values(52,  23, '围棋',       '22', 'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '围棋');
insert into sys_dict_data values(53,  24, '书法',       '23', 'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '书法');
insert into sys_dict_data values(54,  25, '美术',       '24', 'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '美术');
insert into sys_dict_data values(55,  26, '英语口语',   '25', 'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '英语口语');
insert into sys_dict_data values(56,  27, '四级',       '26', 'sys_subject', '', '', 'N', '0', 'admin', sysdate(), '', null, '四级');


-- ==================================================
-- 1. 辅导方式 (sys_methods)
-- ==================================================
insert into sys_dict_data values(60, 1, '线下辅导',     '0', 'sys_methods', '', '', 'N', '0', 'admin', sysdate(), '', null, '线下上门辅导');
insert into sys_dict_data values(61, 2, '网络辅导',     '1', 'sys_methods', '', '', 'N', '0', 'admin', sysdate(), '', null, '线上远程辅导');
insert into sys_dict_data values(62, 3, '线上线下均可', '2', 'sys_methods', '', '', 'N', '0', 'admin', sysdate(), '', null, '可协商决定');

-- ==================================================
-- 2. 年级 (sys_class)
-- ==================================================
-- 学前/小学
insert into sys_dict_data values(70, 1, '学前/幼儿园', '0', 'sys_class', '', '', 'N', '0', 'admin', sysdate(), '', null, '学前阶段');
insert into sys_dict_data values(71, 2, '一年级',      '1', 'sys_class', '', '', 'N', '0', 'admin', sysdate(), '', null, '小学一年级');
insert into sys_dict_data values(72, 3, '二年级',      '2', 'sys_class', '', '', 'N', '0', 'admin', sysdate(), '', null, '小学二年级');
insert into sys_dict_data values(73, 4, '三年级',      '3', 'sys_class', '', '', 'N', '0', 'admin', sysdate(), '', null, '小学三年级');
insert into sys_dict_data values(74, 5, '四年级',      '4', 'sys_class', '', '', 'N', '0', 'admin', sysdate(), '', null, '小学四年级');
insert into sys_dict_data values(75, 6, '五年级',      '5', 'sys_class', '', '', 'N', '0', 'admin', sysdate(), '', null, '小学五年级');
insert into sys_dict_data values(76, 7, '六年级',      '6', 'sys_class', '', '', 'N', '0', 'admin', sysdate(), '', null, '小学六年级');
-- 初中
insert into sys_dict_data values(77, 8, '初一',        '7', 'sys_class', '', '', 'N', '0', 'admin', sysdate(), '', null, '初中一年级');
insert into sys_dict_data values(78, 9, '初二',        '8', 'sys_class', '', '', 'N', '0', 'admin', sysdate(), '', null, '初中二年级');
insert into sys_dict_data values(79, 10, '初三',       '9', 'sys_class', '', '', 'N', '0', 'admin', sysdate(), '', null, '初中三年级');
-- 高中及其他
insert into sys_dict_data values(80, 11, '高一',       '10', 'sys_class', '', '', 'N', '0', 'admin', sysdate(), '', null, '高中一年级');
insert into sys_dict_data values(81, 12, '高二',       '11', 'sys_class', '', '', 'N', '0', 'admin', sysdate(), '', null, '高中二年级');
insert into sys_dict_data values(82, 13, '高三',       '12', 'sys_class', '', '', 'N', '0', 'admin', sysdate(), '', null, '高中三年级');
insert into sys_dict_data values(83, 14, '成人/其他',   '13', 'sys_class', '', '', 'N', '0', 'admin', sysdate(), '', null, '成人教育或其他');

-- ==================================================
-- 3. 学历 (sys_degree)
-- ==================================================
-- 注意：这里的值与您最开始 `tutors` 表里的 `degree` 字段注释约定（0-本科, 1-硕士, 2-博士）保持了逻辑对应，我额外补全了大专和其他。
insert into sys_dict_data values(90, 1, '大专', '0', 'sys_degree', '', '', 'N', '0', 'admin', sysdate(), '', null, '专科学历');
insert into sys_dict_data values(91, 2, '本科', '1', 'sys_degree', '', '', 'N', '0', 'admin', sysdate(), '', null, '本科学历');
insert into sys_dict_data values(92, 3, '硕士', '2', 'sys_degree', '', '', 'N', '0', 'admin', sysdate(), '', null, '硕士研究生');
insert into sys_dict_data values(93, 4, '博士', '3', 'sys_degree', '', '', 'N', '0', 'admin', sysdate(), '', null, '博士研究生');
insert into sys_dict_data values(94, 5, '其他', '4', 'sys_degree', '', '', 'N', '0', 'admin', sysdate(), '', null, '其他学历');








INSERT INTO `sys_user` (
    `user_id`, `dept_id`, `user_name`, `nick_name`, `user_type`, `email`, `phonenumber`, `sex`, `avatar`, `password`, `status`, `del_flag`, `login_ip`, `login_date`, `create_by`, `create_time`, `remark`
) VALUES
-- =======================================
-- 以下为10条教员(Tutors)用户数据 (uid: 1001 - 1010)
-- =======================================
(1001, NULL, 'tutor1001', '张同学', '00', 'zhang1001@example.com', '13800001001', '0', '', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '大学生教员'),
(1002, NULL, 'tutor1002', '李老师', '00', 'li1002@example.com', '13800001002', '1', '', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '专职教师'),
(1003, NULL, 'tutor1003', '王同学', '00', 'wang1003@example.com', '13800001003', '1', '', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '大学生教员'),
(1004, NULL, 'tutor1004', '赵同学', '00', 'zhao1004@example.com', '13800001004', '0', '', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '大学生教员'),
(1005, NULL, 'tutor1005', '陈老师', '00', 'chen1005@example.com', '13800001005', '1', '', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '专职教师'),
(1006, NULL, 'tutor1006', '刘同学', '00', 'liu1006@example.com', '13800001006', '0', '', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '大学生教员'),
(1007, NULL, 'tutor1007', '孙老师', '00', 'sun1007@example.com', '13800001007', '0', '', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '兼职老师'),
(1008, NULL, 'tutor1008', '周同学', '00', 'zhou1008@example.com', '13800001008', '1', '', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '大学生教员'),
(1009, NULL, 'tutor1009', '吴老师', '00', 'wu1009@example.com', '13800001009', '0', '', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '在职教师'),
(1010, NULL, 'tutor1010', '郑同学', '00', 'zheng1010@example.com', '13800001010', '0', '', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '大学生教员'),

-- =======================================
-- 以下为10条家长(Parents)用户数据 (uid: 2001 - 2010)
-- =======================================
(2001, NULL, 'parent2001', '张先生', '00', 'parent2001@example.com', '13900002001', '0', '', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '找初二数学家教'),
(2002, NULL, 'parent2002', '李女士', '00', 'parent2002@example.com', '13900002002', '1', '', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '找高一物理家教'),
(2003, NULL, 'parent2003', '王女士', '00', 'parent2003@example.com', '13900002003', '1', '', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '找小学英语家教'),
(2004, NULL, 'parent2004', '赵先生', '00', 'parent2004@example.com', '13900002004', '0', '', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '找初三全科家教'),
(2005, NULL, 'parent2005', '陈先生', '00', 'parent2005@example.com', '13900002005', '0', '', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '找高二化学家教'),
(2006, NULL, 'parent2006', '刘女士', '00', 'parent2006@example.com', '13900002006', '1', '', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '找五年级奥数家教'),
(2007, NULL, 'parent2007', '孙先生', '00', 'parent2007@example.com', '13900002007', '0', '', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '找初一语文家教'),
(2008, NULL, 'parent2008', '周女士', '00', 'parent2008@example.com', '13900002008', '1', '', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '找高三英语家教'),
(2009, NULL, 'parent2009', '吴女士', '00', 'parent2009@example.com', '13900002009', '1', '', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '找一年级语文家教'),
(2010, NULL, 'parent2010', '郑先生', '00', 'parent2010@example.com', '13900002010', '0', '', 'e10adc3949ba59abbe56e057f20f883e', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '找初二物理家教');

DROP TABLE IF EXISTS `tutors`;
CREATE TABLE `tutors` (   `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '教员表主键ID',
                          `uid` INT NOT NULL UNIQUE COMMENT '关联user表的主键ID',
                          `title` VARCHAR(50) DEFAULT NULL COMMENT '职称（如：教师、老师、大学生教员）',
                          `certificates` VARCHAR(255) DEFAULT NULL COMMENT '证书图片url',
                          `subjects` JSON DEFAULT NULL COMMENT '可授科目数组（使用索引0，1，2)',
                          `areas` JSON DEFAULT NULL COMMENT '可授区域数组（地区索引id）',
                          `methods` VARCHAR(50) DEFAULT NULL COMMENT '授课方式（网络辅导、线下）',
                          `is_certified` VARCHAR(20) DEFAULT '待审核' COMMENT '审核状态（待审核、已通过、已拒绝）',
                          `salary` VARCHAR(50) DEFAULT NULL COMMENT '薪资要求（如：100元/小时）',
                          `experience` TEXT COMMENT '经历/履历',
                          `major` VARCHAR(100) DEFAULT NULL COMMENT '专业',
                          `school` VARCHAR(100) DEFAULT NULL COMMENT '就读/毕业院校',
                          `degree` TINYINT DEFAULT 0 COMMENT '学历枚举：0-本科, 1-硕士, 2-博士',
                          `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='大学生表/教员表';

INSERT INTO `tutors` (`id`,`uid`, `title`, `certificates`, `subjects`, `areas`, `methods`, `is_certified`, `salary`, `experience`, `major`, `school`, `degree`) VALUES
       (01,1001, '大学生教员', 'https://img.example.com/cert1.jpg', '[0, 1]', '[320102, 320104]', '线下', '已通过', '100元/小时', '带过两届初三数学，提分显著', '数学与应用数学', '南京大学', 0),
       (02,1002, '专职教师', 'https://img.example.com/cert2.jpg', '[2]', '[320106]', '网络辅导', '已通过', '200元/小时', '五年高中物理重点班教学经验', '物理学', '东南大学', 1),
       (03,1003, '大学生教员', 'https://img.example.com/cert3.jpg', '[1, 3]', '[320105]', '线下', '待审核', '80元/小时', '英语专八，擅长口语及雅思教学', '英语', '南京师范大学', 0),
       (04,1004, '大学生教员', 'https://img.example.com/cert4.jpg', '[0]', '[320102, 320105]', '线下', '已通过', '120元/小时', '大学期间连续三年获得一等奖学金', '计算机科学', '南京航空航天大学', 0),
       (05,1005, '专职教师', 'https://img.example.com/cert5.jpg', '[4, 5]', '[320104]', '线下', '已拒绝', '150元/小时', '擅长小学全科辅导，有心理咨询师证', '小学教育', '晓庄学院', 0),
       (06,1006, '大学生教员', 'https://img.example.com/cert6.jpg', '[2, 6]', '[320111]', '网络辅导', '已通过', '90元/小时', '高中化学生物竞赛省一等奖', '生物工程', '南京农业大学', 1),
       (07,1007, '兼职老师', 'https://img.example.com/cert7.jpg', '[0, 2]', '[320115]', '线下', '待审核', '110元/小时', '有耐心，善于与青少年沟通', '自动化', '南京理工大学', 1),
       (08,1008, '大学生教员', 'https://img.example.com/cert8.jpg', '[1]', '[320102]', '网络辅导', '已通过', '85元/小时', '高中英语课代表，擅长语法梳理', '翻译', '南京大学', 0),
       (09,1009, '在职教师', 'https://img.example.com/cert9.jpg', '[7]', '[320106]', '线下', '已通过', '300元/小时', '十年高三历史把关经验', '历史学', '南京师范大学', 2),
       (10,1010, '大学生教员', 'https://img.example.com/cert10.jpg', '[0, 8]', '[320104, 320111]', '线下', '已通过', '100元/小时', '中考市排名前500，理科基础扎实', '电子信息', '东南大学', 0);


DROP TABLE IF EXISTS `parents`;
CREATE TABLE `parents` (    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '教员表主键ID',
                           `uid` INT NOT NULL UNIQUE COMMENT '关联user表的主键ID',
                           `location` VARCHAR(255) DEFAULT NULL COMMENT '地理位置文本（如：XX小区）',
                           `geo` VARCHAR(100) DEFAULT NULL COMMENT '经纬度位置（如：118.82,32.04）',
                           `region` VARCHAR(50) DEFAULT NULL COMMENT '区域（如：玄武区）',
                           `name` VARCHAR(150) DEFAULT NULL COMMENT '家教单的名称',
                           `grade` VARCHAR(50) DEFAULT NULL COMMENT '年级（如：一年级，初一）',
                           `subject` VARCHAR(50) DEFAULT NULL COMMENT '科目',
                           `methods` VARCHAR(50) DEFAULT NULL COMMENT '辅导方式（网络辅导、线下）',
                           `requirements` TEXT COMMENT '家长家教需求文本',
                           `brief` TEXT COMMENT '家长孩子情况简介',
                           `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家教订单表';

INSERT INTO `parents` (`id`,`uid`, `location`, `geo`, `region`, `name`, `grade`, `subject`, `methods`, `requirements`, `brief`) VALUES
                                                                                                                               (01,2001, '南京市玄武区中山门大街XX小区', '118.823,32.045', '玄武区', '急招初二数学家教', '8', '8', '0', '要求老师有耐心，能针对基础薄弱点进行讲解', '孩子理科偏科，数学不及格，性格有点内向'),
                                                                                                                               (02,2002, '南京市鼓楼区汉口路XX苑', '118.780,32.055', '鼓楼区', '高一物理周末拔高', '10', '10', '1', '希望是985高校理工科专业学生，逻辑严密', '男生，基础不错，需要攻克压轴题'),
                                                                                                                               (03,2003, '南京市建邺区江东中路XX号', '118.730,32.001', '建邺区', '小学三年级英语口语陪练', '3', '7', '0', '发音标准，能带孩子读英语绘本并互动', '女孩，比较活泼好动，需要老师有趣味性'),
                                                                                                                               (04,2004, '南京市秦淮区夫子庙街道XX大院', '118.790,32.022', '秦淮区', '初三理科周末冲刺', '9', '2', '0', '需要有中考辅导经验的老师，能帮忙做学习计划', '面临中考，学习压力大，需要心理疏导和方法指导'),
                                                                                                                               (05,2005, '无明确地址', '0,0', '栖霞区', '高二化学线上答疑', '11', '11', '1', '晚上9点到10点在线解答作业难题', '孩子住校，只有晚上能用平板，主要问错题'),
                                                                                                                               (06,2006, '南京市雨花台区软件大道XX小区', '118.765,31.980', '雨花台区', '五年级奥数启蒙', '5', '9', '0', '有过竞赛经验优先，不要死记硬背公式', '数学成绩优异，想提前接触拓展内容'),
                                                                                                                               (07,2007, '南京市江宁区龙眠大道XX号', '118.815,31.912', '江宁区', '初一语文阅读与写作辅导', '7', '6', '0', '文科类专业，擅长引导孩子阅读和积累素材', '作文经常跑题，阅读理解失分严重'),
                                                                                                                               (08,2008, '海外/异地', '0,0', '玄武区', '高三英语听力口语突击', '12', '7', '1', '英语专八或有留学背景，发音纯正', '准备走中外合作办学，需要强化听说能力'),
                                                                                                                               (09,2009, '南京市浦口区珠江镇XX小区', '118.630,32.060', '浦口区', '一年级拼音认字辅导', '1', '6', '0', '幼师或者小学教育专业优先，亲和力强', '刚上小学，跟不上进度，拼音总是搞混'),
                                                                                                                               (10,2010, '南京市六合区雄州街道XX苑', '118.840,32.340', '六合区', '初二物理入门辅导', '8', '10', '0', '需要老师自带一些小实验道具，激发兴趣', '刚接触物理，觉得比较抽象，需要培养物理思维');


-- 课程表
CREATE TABLE `lectures` (
                            `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '唯一编码',
                            `time` DATETIME NOT NULL COMMENT '开讲时间',
                            `name` VARCHAR(255) NOT NULL COMMENT '课程名称',
                            `speaker` VARCHAR(100) DEFAULT NULL COMMENT '讲师名称',
                            `location` VARCHAR(255) DEFAULT NULL COMMENT '详细地址',
                            `geo` VARCHAR(100) DEFAULT NULL COMMENT '经纬度信息 (如: 118.80,32.05)',
                            `detail` TEXT COMMENT '活动详情',
                            `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程活动/讲座表';

INSERT INTO `lectures` (`time`, `name`, `speaker`, `location`, `geo`, `detail`) VALUES
-- 较早的历史活动
('2025-10-15 14:00:00', '中考数学压轴题专项拆解', '张名师', '南京市玄武区图书馆一楼报告厅', '118.800,32.050', '针对历年中考数学最后两道大题的专项拆解与训练，适合初三学生。'),
('2025-12-20 09:30:00', '青少年心理健康与家庭教育', '李教授', '南京市鼓楼区文化活动中心', '118.770,32.060', '探讨青春期孩子的心理变化、叛逆期表现及家长的科学应对策略。'),

-- 刚刚过去的近期活动
('2026-02-28 15:00:00', '高效记忆法与英语词汇突破', '王讲师', '南京市建邺区青年创客空间', '118.740,32.010', '分享实用的记忆宫殿法与词根词缀记忆法，帮助学生快速扩大英语词汇量。'),

-- 即将到来的近期活动
('2026-03-08 19:00:00', '新高考志愿填报权威指南', '赵专家', '南京市秦淮区教育局大礼堂', '118.790,32.025', '全面解读最新高考政策与选科要求，指导高三学生及家长科学填报志愿，规避滑档风险。'),

-- 未来的活动
('2026-04-10 10:00:00', '少儿编程启蒙与人工智能', '孙工程师', '南京市雨花台区软件谷科技展厅', '118.760,31.980', '面向小学生的 Scratch 编程基础体验课，结合简单的 AI 互动，激发科技创新兴趣。');





