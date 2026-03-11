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
create table tutors
(
    id           bigint auto_increment comment '教员表主键ID'
        primary key,
    uid          int                                not null comment '关联user表的主键ID',
    title        varchar(20)                        null comment '职称（如：教师、老师、大学生教员）',
    certificates varchar(255)                       null comment '证书图片url',
    subjects     varchar(40)                        null comment '可授科目数组（使用索引0，1，2)',
    areas        varchar(100)                       null comment '可授区域数组（地区索引id）',
    methods      int                                null comment '授课方式（网络辅导、线下）',
    is_certified int      default 0                 null comment '审核状态（待审核、已通过、已拒绝）',
    salary       varchar(50)                        null comment '薪资要求（如：100元/小时）',
    experience   text                               null comment '经历/履历',
    major        varchar(15)                        null comment '专业',
    school       varchar(15)                        null comment '就读/毕业院校',
    degree       tinyint  default 0                 null comment '学历枚举：0-本科, 1-硕士, 2-博士',
    create_date  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_date  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    real_name    varchar(10)                        null comment '实名认证真实姓名',
    id_card      varchar(20)                        null comment '身份证号码',
    self_judge   text                               null comment '个人评价',
    certificate  text                               null comment '证书',
    live         varchar(10)                        null comment '生活区域',
    work         varchar(10)                        null,
    constraint uid
        unique (uid)
)
    comment '大学生表/教员表';


INSERT INTO tutors (uid, title, certificates, subjects, areas, methods, is_certified, salary, experience, major, school, degree, real_name, id_card, self_judge, certificate, live, work)
VALUES
    (1001, '大学生教员', 'https://img.com/c1.jpg', '0,1,2', '320115,320114', 1, 1, '100-150元/小时',
     '2023.09至今在高三进行数学提分辅导，学生成绩从85提升至112分；2022年曾带初中奥数班。',
     '数学与应用数学', '南京大学', 0, '张三', '320101199901011234', '认真负责，擅长逻辑引导', '英语六级', '江宁区', '雨花台区'),

    (1002, '专业教师', 'https://img.com/c2.jpg', '3,5', '320102,320104', 0, 1, '200元/小时',
     '10年高中物理教学经验，曾任职于南京某重点中学，带过三届高三毕业班，熟悉高考考点。',
     '物理学', '南京师范大学', 1, '李四', '320101199505052345', '教学幽默，深受学生喜爱', '高级教师资格证', '玄武区', '秦淮区'),

    (1003, '大学生教员', NULL, '1,4', '320113', 1, 0, '80-120元/小时',
     '大二开始从事家教，辅导过3名小学生的语数英全科作业，有良好的沟通能力。',
     '英语', '南京航空航天大学', 0, '王五', '320101200208083456', '发音标准，亲和力强', '专八证书', '栖霞区', '栖霞区'),

    (1004, '金牌教员', 'https://img.com/c4.jpg', '0,8', '320106,320105', 1, 1, '300元/小时',
     '长期担任奥数竞赛教练，多名学员获得省级一等奖；精通高中数学难题拆解。',
     '计算机科学', '东南大学', 2, '赵六', '320101199012124567', '逻辑极强，擅长难题拆解', '博士学位证', '鼓楼区', '建邺区'),

    (1005, '大学生教员', NULL, '10,11', '320104', 0, 2, '100元/小时',
     '钢琴十级，在南京艺术学院就读期间长期兼职钢琴陪练，负责纠正指法。',
     '音乐表演', '南京艺术学院', 0, '孙七', '320101200103035678', '极具耐心，艺术气息浓厚', '钢琴十级证书', '秦淮区', '秦淮区'),

    (1006, '专业教员', 'https://img.com/c6.jpg', '2,6', '320111', 1, 1, '150元/小时',
     '南京本地化学老师，擅长初三化学考前冲刺，能快速帮助学生建立知识体系。',
     '应用化学', '南京理工大学', 1, '周八', '320101199604046789', '重点难点把握精准', '教师资格证', '浦口区', '浦口区'),

    (1007, '大学生教员', NULL, '15', '320115', 0, 0, '120元/小时',
     '研二学生，考研政治88分，熟悉考研政治大纲，有一套独特的背诵方法。',
     '马克思主义理论', '河海大学', 1, '吴九', '320101199811117890', '政治理论扎实，经验丰富', '优秀毕业生', '江宁区', '江宁区'),

    (1008, '专业教师', 'https://img.com/c8.jpg', '7,9', '320116', 1, 1, '250元/小时',
     '资深生物老师，整理有全套初中生物知识归纳笔记，尤其擅长实验题讲解。',
     '生物科学', '中国药科大学', 1, '郑十', '320101199402028901', '严谨负责，注重知识联想', '高级职称证', '六合区', '江宁区'),

    (1009, '大学生教员', NULL, '20', '320115', 0, 1, '90元/小时',
     '擅长少儿编程机器人辅导，曾带队参加省青少年科技创新大赛。',
     '软件工程', '南京邮电大学', 0, '钱十一', '320101200306069012', '思维活跃，互动性强', '省赛一等奖', '栖霞区', '江宁区'),

    (1010, '大学生教员', 'https://img.com/c10.jpg', '4,1', '320114', 1, 1, '180元/小时',
     '英语专业八级，雅思8.0，曾在南京某知名培训机构兼职托福口语老师。',
     '英语教育', '南京师范大学', 1, 'Mike', '320101199709091111', '发音纯正，口语地道', 'TESOL证书', '雨花台区', '雨花台区');



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

drop table if exists lectures;
-- 课程表
CREATE TABLE `lectures` (
                            `id` bigint(20) AUTO_INCREMENT PRIMARY KEY COMMENT '唯一编码',
                            `time` DATETIME NOT NULL COMMENT '开讲时间',
                            `end_date` DATETIME DEFAULT NULL COMMENT '会议结束日期',
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



CREATE TABLE daily_jobs (
                            id           BIGINT AUTO_INCREMENT COMMENT '日结工作主键ID' PRIMARY KEY,
                            title        VARCHAR(50) NOT NULL COMMENT '工作标题（如：初中数学日结兼职）',
                            category     TINYINT DEFAULT 0 COMMENT '分类：0-家教, 1-助教, 2-派发, 3-其他',
                            salary_day   DECIMAL(10, 2) NOT NULL COMMENT '日结薪水（元/日）',
                            work_date    DATE NOT NULL COMMENT '工作具体日期',
                            work_time    VARCHAR(50) COMMENT '具体时间段（如：14:00-16:00）',
                            location     VARCHAR(100) NOT NULL COMMENT '工作详细地址',
                            district_id  VARCHAR(10) COMMENT '区域区号（如：320115）',
                            contacts     VARCHAR(20) COMMENT '联系人姓名',
                            phone        VARCHAR(20) COMMENT '联系电话',
                            description  TEXT COMMENT '工作具体要求内容',
                            status       TINYINT DEFAULT 0 COMMENT '状态：0-招募中, 1-已满员, 2-已结束',
                            create_time  DATETIME DEFAULT CURRENT_TIMESTAMP,
                            update_time  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '兼职日结工作表';

INSERT INTO daily_jobs (title, category, salary_day, work_date, work_time, location, district_id, contacts, phone, description, status)
VALUES
    ('高三数学考前突击', 0, 300.00, '2026-03-10', '18:00-20:00', '江宁区大学城某小区', '320115', '张家长', '13800001111', '急需一名数学优秀的教员，针对导数大题进行讲解。', 0),
    ('少儿编程体验课助教', 1, 150.00, '2026-03-11', '09:00-12:00', '秦淮区新街口某机构', '320104', '李老师', '13800002222', '协助主讲老师维护课堂秩序，分发器材。', 0),
    ('初中物理实验演示', 0, 200.00, '2026-03-10', '14:00-16:00', '鼓楼区北京西路', '320106', '王女士', '13800003333', '演示初二物理电路实验，要求操作规范。', 0),
    ('教育展会宣传员', 2, 120.00, '2026-03-12', '08:30-17:30', '建邺区国际博览中心', '320105', '赵经理', '13800004444', '负责展台宣传单页派发，形象气质佳优先。', 1),
    ('钢琴陪练（日结）', 0, 180.00, '2026-03-10', '19:00-20:00', '雨花台区软件大道', '320114', '孙先生', '13800005555', '陪同7岁孩子练习钢琴，纠正手型。', 0),
    ('英语口语陪练', 0, 260.00, '2026-03-13', '15:00-17:00', '栖霞区仙林大学城', '320113', '刘同学', '13800006666', '纯英文对话，练习雅思口语部分。', 0),
    ('书法班代课老师', 1, 220.00, '2026-03-14', '10:00-12:00', '玄武区珠江路', '320102', '周校长', '13800007777', '要求有硬笔书法基础，代课一小节。', 0),
    ('考研专业课答疑', 0, 400.00, '2026-03-10', '20:00-22:00', '线上辅导（南京本地优先）', '320100', '钱同学', '13800008888', '解答南大专业课真题，要求研究生在读。', 0),
    ('周末户外写生助教', 1, 160.00, '2026-03-15', '09:00-16:00', '南京玄武湖公园', '320102', '吴老师', '13800009999', '协助照顾外出写生的小朋友，注意安全。', 0),
    ('小学奥数临时辅导', 0, 200.00, '2026-03-11', '16:30-18:30', '六合区龙津路', '320116', '郑家长', '13800000000', '针对晚托班学生进行奥数难题点拨。', 0);


CREATE TABLE `user_realname_auth` (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                      `uid` bigint(20) NOT NULL COMMENT '用户ID，关联sys_user表',
                                      `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
                                      `id_card` varchar(18) NOT NULL COMMENT '身份证号码',
                                      `auth_status` tinyint(4) DEFAULT '0' COMMENT '认证状态：0-待审核，1-已通过，2-已驳回',
                                      `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `uk_uid` (`uid`) COMMENT '保证一个用户只能有一条认证记录',
                                      UNIQUE KEY `uk_id_card` (`id_card`) COMMENT '保证一个身份证号只能认证一次',
                                      CONSTRAINT `fk_auth_sys_user` FOREIGN KEY (`uid`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户实名认证表';

INSERT INTO `user_realname_auth` (`uid`, `real_name`, `id_card`, `auth_status`) VALUES
-- 10xx 序列 (导师/学生)
(1001, '张同学', '110105200501010011', 1),
(1002, '李老师', '110105199001020022', 1),
(1003, '王同学', '110105200501030033', 1),
(1004, '赵同学', '110105200501040044', 1),
(1005, '陈老师', '110105199001050055', 1),
(1006, '刘同学', '110105200501060066', 1),
(1007, '孙老师', '110105199001070077', 1),
(1008, '周同学', '110105200501080088', 1),
(1009, '吴老师', '110105199001090099', 1),
(1010, '郑同学', '11010520050110010X', 1),

-- 20xx 序列 (家长)
(2001, '张先生', '110105198001010011', 1),
(2002, '李女士', '110105198001020022', 1),
(2003, '王女士', '110105198001030033', 1),
(2004, '赵先生', '110105198001040044', 1),
(2005, '陈先生', '110105198001050055', 1),
(2006, '刘女士', '110105198001060066', 1),
(2007, '孙先生', '110105198001070077', 1),
(2008, '周女士', '110105198001080088', 1),
(2009, '吴女士', '110105198001090099', 1),
(2010, '郑先生', '11010519800110010X', 1);


-- ==================================================
-- 签到与报名记录表（兼容讲座签到 + 沙龙报名两种场景）
-- record_type=1: 讲座签到，lecture_id 有值
-- record_type=2: 沙龙报名，salon_id 有值，contact_name/phone 有值
-- ==================================================
drop table if exists `sign_in_record`;
CREATE TABLE `sign_in_record` (
    `id`            BIGINT(20)   NOT NULL AUTO_INCREMENT                COMMENT '主键ID',
    `uid`           BIGINT(20)   NOT NULL                               COMMENT '用户ID，关联sys_user表的user_id',
    `record_type`   TINYINT(4)   NOT NULL DEFAULT 1                    COMMENT '记录类型: 1-讲座签到, 2-沙龙报名',
    `lecture_id`    BIGINT(20)   DEFAULT NULL                           COMMENT '讲座ID，关联lectures表（record_type=1时有值）',
    `salon_id`      BIGINT(20)   DEFAULT NULL                           COMMENT '沙龙ID，关联salon_info表（record_type=2时有值）',
    `sign_time`     DATETIME     DEFAULT CURRENT_TIMESTAMP              COMMENT '签到/报名时间',
    `sign_status`   TINYINT(4)   DEFAULT 0                             COMMENT '状态: 0-已报名/待签到, 1-已签到/正常, 2-迟到, 3-已取消',
    `contact_name`  VARCHAR(50)  DEFAULT NULL                           COMMENT '联系人姓名（沙龙报名时填写）',
    `contact_phone` VARCHAR(20)  DEFAULT NULL                           COMMENT '联系电话（沙龙报名时填写）',
    `remark`        VARCHAR(255) DEFAULT NULL                           COMMENT '备注信息',
    `device_info`   VARCHAR(255) DEFAULT NULL                           COMMENT '签到设备或IP（可选，用于防作弊）',
    PRIMARY KEY (`id`),
    INDEX `idx_uid_type`   (`uid`, `record_type`) COMMENT '按用户和类型查询',
    INDEX `idx_lecture_id` (`lecture_id`)          COMMENT '按讲座查询签到',
    INDEX `idx_salon_id`   (`salon_id`)            COMMENT '按沙龙查询报名'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='签到与报名记录表（讲座签到/沙龙报名）';




-- activity_salon 表已废弃，统一使用 salon_info 表管理沙龙活动数据（含价格、封面、报名等）



-- ----------------------------
-- 1. 问卷调查表 (questionnaire)
-- ----------------------------
CREATE TABLE `questionnaire` (
                                 `id` BIGINT NOT NULL COMMENT '主键ID (雪花算法)',
                                 `lecture_id` BIGINT NOT NULL COMMENT '关联课程ID',
                                 `url` VARCHAR(512) NOT NULL COMMENT '问卷星链接URL',
                                 `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-失效, 1-有效 (配合Java定时任务控制开课7天内有效)',
                                 `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 `is_deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除标识: 0-未删除, 1-已删除',
                                 PRIMARY KEY (`id`),
                                 INDEX `idx_lecture_status` (`lecture_id`, `status`) COMMENT '联合索引加速根据课程查询有效问卷'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问卷调查配置表';

-- ----------------------------
-- 2. 资料中心数据表 (lecture_material)
-- ----------------------------
CREATE TABLE `lecture_material` (
                                    `id` BIGINT NOT NULL COMMENT '主键ID',
                                    `lecture_id` BIGINT NOT NULL COMMENT '关联课程ID',
                                    `url` VARCHAR(512) NOT NULL COMMENT '资料/PDF文件OSS链接',
                                    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-隐藏, 1-展示 (由后端接口业务调整)',
                                    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `is_deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除标识',
                                    PRIMARY KEY (`id`),
                                    INDEX `idx_lecture_id` (`lecture_id`) COMMENT '加速课程资料拉取'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资料中心数据表';

-- ----------------------------
-- 3. 讲师风采表 (lecturer_profile)
-- ----------------------------
CREATE TABLE `lecturer_profile` (
                                    `id` BIGINT NOT NULL COMMENT '主键ID',
                                    `name` VARCHAR(64) NOT NULL COMMENT '讲师姓名',
                                    `intro` TEXT COMMENT '讲师简介/职位介绍',
                                    `avatar_url` VARCHAR(512) COMMENT '头像URL',
                                    `poster_url` VARCHAR(512) COMMENT '宣传海报URL',
                                    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `is_deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除标识',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='讲师风采表';

-- ----------------------------
-- 4. 学籍信息表 (student_enrollment)
-- ----------------------------
CREATE TABLE `student_enrollment` (
                                      `id` BIGINT NOT NULL COMMENT '主键ID',
                                      `uid` BIGINT NOT NULL COMMENT '用户ID (关联小程序学员)',
                                      `lecture_id` BIGINT NOT NULL COMMENT '关联课程ID',
                                      `total` INT NOT NULL DEFAULT 0 COMMENT '总学籍数/报名数',
                                      `remain` INT NOT NULL DEFAULT 0 COMMENT '剩余可用学籍数 (支持线下核销或赠送扣减)',
                                      `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      `is_deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除标识',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `uk_uid_lecture` (`uid`, `lecture_id`) COMMENT '联合唯一约束：同一学员同一课程只保留一条汇总记录',
                                      INDEX `idx_uid` (`uid`) COMMENT '加速查询"我的学籍"'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学籍信息表';


-- 插入讲师风采数据 (假设课程由该讲师讲授)
INSERT INTO `lecturer_profile` (`id`, `name`, `intro`, `avatar_url`, `poster_url`) VALUES
                                                                                       (1898700000000001, '李老师', '资深心理学导师，拥有10年家庭关系咨询经验。', 'https://cdn.example.com/avatar/li.png', 'https://cdn.example.com/poster/li_poster.jpg'),
                                                                                       (1898700000000002, '王教授', '亲子教育专家，主讲青少年潜能开发沙龙。', 'https://cdn.example.com/avatar/wang.png', 'https://cdn.example.com/poster/wang_poster.jpg');

-- 插入问卷调查数据 (关联虚拟课程ID: 9901 代表幸福解码)
-- 设定为状态 1 (有效)，代表开课 7 天内的评估问卷 [cite: 1]
INSERT INTO `questionnaire` (`id`, `lecture_id`, `url`, `status`) VALUES
                                                                      (1898700000000010, 9901, 'https://www.wjx.cn/vm/exAmple1.aspx', 1),
                                                                      (1898700000000011, 9902, 'https://www.wjx.cn/vm/exAmple2.aspx', 0); -- 0表示已失效（超过7天）

-- 插入资料中心数据 (关联同样的课程ID)
INSERT INTO `lecture_material` (`id`, `lecture_id`, `url`, `status`) VALUES
                                                                         (1898700000000020, 9901, 'https://cdn.example.com/pdf/幸福解码思维导图.pdf', 1),
                                                                         (1898700000000021, 9901, 'https://cdn.example.com/pdf/家庭关系核心讲义.pdf', 1),
                                                                         (1898700000000022, 9902, 'https://cdn.example.com/pdf/往期沙龙回顾.pdf', 0); -- 0表示接口隐藏

-- 插入学籍信息数据
-- 模拟学员A (uid: 8801) 购买了合伙人套餐，拥有5个《幸福解码》名额，目前消耗了1个，剩余4个
INSERT INTO `student_enrollment` (`id`, `uid`, `lecture_id`, `total`, `remain`) VALUES
                                                                                    (1898700000000030, 8801, 9901, 5, 4),
                                                                                    (1898700000000031, 8802, 9901, 1, 1),
                                                                                    (1898700000000032, 8801, 9902, 2, 0); -- 剩余0表示已全部分享或核销完


CREATE TABLE `salon_info` (
                              `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '沙龙ID，主键',
                              `title` VARCHAR(128) NOT NULL COMMENT '沙龙主标题，如：组局思维',
                              `subtitle` VARCHAR(128) DEFAULT NULL COMMENT '副标题或标签，如：沙龙/社群/KOL',
                              `cover_img` VARCHAR(512) DEFAULT NULL COMMENT '封面图的URL',
                              `description` TEXT COMMENT '详情页内容（富文本HTML或JSON）',
                              `original_price` DECIMAL(10,2) DEFAULT '0.00' COMMENT '原价/划线价，如：768.00',
                              `current_price` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '实际售卖价，如：128.00',
                              `start_time` DATETIME DEFAULT NULL COMMENT '沙龙举办/开始时间',
                              `sales_volume` INT NOT NULL DEFAULT '0' COMMENT '已售数量（用于页面展示）',
                              `status` TINYINT NOT NULL DEFAULT '1' COMMENT '状态：0-下架草稿，1-上架售卖中',
                              `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='沙龙活动信息主表';


-- ==================================================
-- 用户钱包表（余额 / 冻结 / 累计）
-- ==================================================
DROP TABLE IF EXISTS `user_wallet`;
CREATE TABLE `user_wallet` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT               COMMENT '主键ID',
    `uid`             BIGINT       NOT NULL                              COMMENT '用户ID，关联sys_user表',
    `balance`         DECIMAL(10,2) NOT NULL DEFAULT 0.00               COMMENT '可用余额（元）',
    `frozen`          DECIMAL(10,2) NOT NULL DEFAULT 0.00               COMMENT '冻结金额（元，提现审核中）',
    `total_earned`    DECIMAL(10,2) NOT NULL DEFAULT 0.00               COMMENT '累计收入（元）',
    `total_withdrawn` DECIMAL(10,2) NOT NULL DEFAULT 0.00               COMMENT '累计提现（元）',
    `create_time`     DATETIME     DEFAULT CURRENT_TIMESTAMP             COMMENT '创建时间',
    `update_time`     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户钱包表';

-- ==================================================
-- 钱包提现申请记录表
-- ==================================================
DROP TABLE IF EXISTS `wallet_withdraw`;
CREATE TABLE `wallet_withdraw` (
    `id`             BIGINT       NOT NULL AUTO_INCREMENT                COMMENT '主键ID',
    `uid`            BIGINT       NOT NULL                               COMMENT '用户ID',
    `amount`         DECIMAL(10,2) NOT NULL                             COMMENT '申请提现金额（元）',
    `status`         TINYINT      NOT NULL DEFAULT 0                    COMMENT '状态: 0-审核中, 1-已打款, 2-已拒绝',
    `remark`         VARCHAR(255) DEFAULT NULL                           COMMENT '备注（拒绝原因等）',
    `wx_transfer_no` VARCHAR(64)  DEFAULT NULL                           COMMENT '微信企业付款转账单号',
    `create_time`    DATETIME     DEFAULT CURRENT_TIMESTAMP              COMMENT '申请时间',
    `update_time`    DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_uid`    (`uid`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='钱包提现申请记录表';


drop table if exists `order`;
CREATE TABLE `trade_order` (
                                  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '内部订单ID，主键',
                                  `order_no` VARCHAR(64) NOT NULL COMMENT '外部展示及支付网关使用的订单号（需唯一）',
                                  `user_id` BIGINT NOT NULL COMMENT '购买用户的唯一标识',

    -- 新增的核心区分字段
                                  `order_type` TINYINT NOT NULL COMMENT '业务类型：1-沙龙(salon订单)，2-讲座(lecture订单)',

    -- 业务关联ID（注意：这里改成了允许为空，因为一笔订单通常只有一个ID有值）
                                  `salon_id` BIGINT DEFAULT NULL COMMENT '关联的沙龙ID（当order_type=1时有值）',
                                  `lecture_id` BIGINT DEFAULT NULL COMMENT '关联的讲座ID（当order_type=2时有值）',

                                  `pay_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '实际支付金额',
                                  `pay_method` VARCHAR(32) DEFAULT NULL COMMENT '支付方式，如：wechat_pay, alipay, offline',
                                  `purpose` VARCHAR(128) DEFAULT NULL COMMENT '主要的用途/备注，如：报名听课、赞助商',
                                  `pay_status` TINYINT NOT NULL DEFAULT '0' COMMENT '支付状态：0-待支付，1-已支付，2-已退款，3-已取消',
                                  `pay_time` DATETIME DEFAULT NULL COMMENT '实际完成支付的时间',
                                  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
                                  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '订单更新时间',
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `uk_order_no` (`order_no`),
                                  KEY `idx_user_id` (`user_id`),
                                  KEY `idx_salon_id` (`salon_id`),
                                  KEY `idx_lecture_id` (`lecture_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通用交易订单表（包含沙龙和讲座）';

INSERT INTO `salon_info` (`id`, `title`, `subtitle`, `cover_img`, `original_price`, `current_price`, `start_time`, `sales_volume`, `status`)
VALUES
    (1, '组局思维', '沙龙/社群/KOL', 'https://demo.com/img1.jpg', 768.00, 128.00, '2026-03-06 14:00:00', 9, 1),
    (2, '岛上圆桌派·第33期', '深度链接/头脑风暴', 'https://demo.com/img2.jpg', 588.00, 58.00, '2026-03-10 19:30:00', 42, 1),
    (3, '春节不打烊：10天让你朋友圈会说话', '个人品牌建设', 'https://demo.com/img3.jpg', 599.00, 198.00, '2026-02-10 10:00:00', 105, 3), -- 状态3模拟已结束
    (4, '手碟音乐工作坊', '你的第一支音乐MV', 'https://demo.com/img4.jpg', 698.00, 128.00, '2026-03-15 14:00:00', 15, 1),
    (5, 'AIGC赋能职场效率沙龙', '效率工具实战', 'https://demo.com/img5.jpg', 299.00, 99.00, '2026-03-20 14:00:00', 0, 0); -- 状态0模拟草稿未上架