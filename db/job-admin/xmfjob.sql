
CREATE TABLE XXL_JOB_QRTZ_JOB_DETAILS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    JOB_NAME  VARCHAR(200) NOT NULL,
    JOB_GROUP VARCHAR(200) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    JOB_CLASS_NAME   VARCHAR(250) NOT NULL,
    IS_DURABLE VARCHAR(1) NOT NULL,
    IS_NONCONCURRENT VARCHAR(1) NOT NULL,
    IS_UPDATE_DATA VARCHAR(1) NOT NULL,
    REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
);

CREATE TABLE XXL_JOB_QRTZ_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    JOB_NAME  VARCHAR(200) NOT NULL,
    JOB_GROUP VARCHAR(200) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    NEXT_FIRE_TIME BIGINT(13) NULL,
    PREV_FIRE_TIME BIGINT(13) NULL,
    PRIORITY INTEGER NULL,
    TRIGGER_STATE VARCHAR(16) NOT NULL,
    TRIGGER_TYPE VARCHAR(8) NOT NULL,
    START_TIME BIGINT(13) NOT NULL,
    END_TIME BIGINT(13) NULL,
    CALENDAR_NAME VARCHAR(200) NULL,
    MISFIRE_INSTR SMALLINT(2) NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
        REFERENCES XXL_JOB_QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP)
);

CREATE TABLE XXL_JOB_QRTZ_SIMPLE_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    REPEAT_COUNT BIGINT(7) NOT NULL,
    REPEAT_INTERVAL BIGINT(12) NOT NULL,
    TIMES_TRIGGERED BIGINT(10) NOT NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES XXL_JOB_QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE XXL_JOB_QRTZ_CRON_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    CRON_EXPRESSION VARCHAR(200) NOT NULL,
    TIME_ZONE_ID VARCHAR(80),
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES XXL_JOB_QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE XXL_JOB_QRTZ_SIMPROP_TRIGGERS
  (          
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR(1) NULL,
    BOOL_PROP_2 VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
    REFERENCES XXL_JOB_QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE XXL_JOB_QRTZ_BLOB_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    BLOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES XXL_JOB_QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE XXL_JOB_QRTZ_CALENDARS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    CALENDAR_NAME  VARCHAR(200) NOT NULL,
    CALENDAR BLOB NOT NULL,
    PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
);

CREATE TABLE XXL_JOB_QRTZ_PAUSED_TRIGGER_GRPS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_GROUP  VARCHAR(200) NOT NULL, 
    PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
);

CREATE TABLE XXL_JOB_QRTZ_FIRED_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    ENTRY_ID VARCHAR(95) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    INSTANCE_NAME VARCHAR(200) NOT NULL,
    FIRED_TIME BIGINT(13) NOT NULL,
    SCHED_TIME BIGINT(13) NOT NULL,
    PRIORITY INTEGER NOT NULL,
    STATE VARCHAR(16) NOT NULL,
    JOB_NAME VARCHAR(200) NULL,
    JOB_GROUP VARCHAR(200) NULL,
    IS_NONCONCURRENT VARCHAR(1) NULL,
    REQUESTS_RECOVERY VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,ENTRY_ID)
);

CREATE TABLE XXL_JOB_QRTZ_SCHEDULER_STATE
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    INSTANCE_NAME VARCHAR(200) NOT NULL,
    LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
    CHECKIN_INTERVAL BIGINT(13) NOT NULL,
    PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
);

CREATE TABLE XXL_JOB_QRTZ_LOCKS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    LOCK_NAME  VARCHAR(40) NOT NULL, 
    PRIMARY KEY (SCHED_NAME,LOCK_NAME)
);



CREATE TABLE `XXL_JOB_QRTZ_TRIGGER_INFO` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_cron` varchar(128) NOT NULL COMMENT '任务执行CRON',
  `job_desc` varchar(255) NOT NULL,
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `author` varchar(64) DEFAULT NULL COMMENT '作者',
  `alarm_email` varchar(255) DEFAULT NULL COMMENT '报警邮件',
  `executor_route_strategy` varchar(50) DEFAULT NULL COMMENT '执行器路由策略',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_block_strategy` varchar(50) DEFAULT NULL COMMENT '阻塞处理策略',
  `executor_timeout` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行超时时间，单位秒',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `glue_type` varchar(50) NOT NULL COMMENT 'GLUE类型',
  `glue_source` text COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) DEFAULT NULL COMMENT 'GLUE备注',
  `glue_updatetime` datetime DEFAULT NULL COMMENT 'GLUE更新时间',
  `child_jobid` varchar(255) DEFAULT NULL COMMENT '子任务ID，多个逗号分隔',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `XXL_JOB_QRTZ_TRIGGER_LOG` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `executor_address` varchar(255) DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `trigger_time` datetime DEFAULT NULL COMMENT '调度-时间',
  `trigger_code` int(11) NOT NULL COMMENT '调度-结果',
  `trigger_msg` varchar(2048) DEFAULT NULL COMMENT '调度-日志',
  `handle_time` datetime DEFAULT NULL COMMENT '执行-时间',
  `handle_code` int(11) NOT NULL COMMENT '执行-状态',
  `handle_msg` varchar(2048) DEFAULT NULL COMMENT '执行-日志',
  PRIMARY KEY (`id`),
  KEY `I_trigger_time` (`trigger_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `XXL_JOB_QRTZ_TRIGGER_LOGGLUE` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `glue_type` varchar(50) DEFAULT NULL COMMENT 'GLUE类型',
  `glue_source` text COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) NOT NULL COMMENT 'GLUE备注',
  `add_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE XXL_JOB_QRTZ_TRIGGER_REGISTRY (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `registry_group` varchar(255) NOT NULL,
  `registry_key` varchar(255) NOT NULL,
  `registry_value` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `XXL_JOB_QRTZ_TRIGGER_GROUP` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(64) NOT NULL COMMENT '执行器AppName',
  `title` varchar(12) NOT NULL COMMENT '执行器名称',
  `order` tinyint(4) NOT NULL DEFAULT '0' COMMENT '排序',
  `address_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '执行器地址类型：0=自动注册、1=手动录入',
  `address_list` varchar(512) DEFAULT NULL COMMENT '执行器地址列表，多地址逗号分隔',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `XXL_JOB_QRTZ_TRIGGER_GROUP` ( `app_name`, `title`, `order`, `address_type`, `address_list`) values ( 'xxl-job-executor-sample', '示例执行器', '1', '0', null);
commit;


-- ----------------------------------------菜单-------------------------------
DROP TABLE IF EXISTS `t_sys_job_menu`;
CREATE TABLE `t_sys_job_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(200) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(2000) DEFAULT NULL COMMENT '菜单地址',
  `isbutton` int(1) DEFAULT '0' COMMENT '是否按钮 0不是 1是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `flag` int(1) DEFAULT '-1' COMMENT '删除标记 -1删除 1正常',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `fid` bigint(20) DEFAULT '-1' COMMENT '父级菜单ID',
  `level` int(11) DEFAULT '1' COMMENT '菜单等级',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='菜单数据表';
-- ----------------------------
-- Records of t_sys_job_menu
-- ----------------------------
INSERT INTO `t_sys_job_menu` VALUES ('1', '系统管理', null, '0', '2018-12-20 21:45:31', '2018-12-20 23:02:55', '1', '系统管理', '-1', '1');
INSERT INTO `t_sys_job_menu` VALUES ('2', '菜单管理', '/job/jobMenu', '0', '2018-12-20 21:46:20', '2018-12-20 21:46:23', '1', '菜单管理', '1', '2');
INSERT INTO `t_sys_job_menu` VALUES ('3', '调度管理', null, '0', '2018-12-20 22:32:27', '2018-12-20 22:39:04', '1', '调度任务', '-1', '1');
INSERT INTO `t_sys_job_menu` VALUES ('4', '调度任务', '/job/jobinfo', '0', '2018-12-20 22:56:43', '2018-12-20 22:56:43', '1', '调度任务', '3', '2');
INSERT INTO `t_sys_job_menu` VALUES ('5', '调度日志', '/job/joblog', '0', '2018-12-20 22:57:29', '2018-12-20 22:57:29', '1', '调度日志', '3', '2');
INSERT INTO `t_sys_job_menu` VALUES ('6', '调度报表', '/job', '0', '2018-12-20 22:58:29', '2018-12-20 22:58:29', '1', '调度报表', '3', '2');
INSERT INTO `t_sys_job_menu` VALUES ('7', '系统监控', null, '0', '2018-12-20 23:00:54', '2018-12-20 23:00:54', '1', '系统监控', '-1', '1');
INSERT INTO `t_sys_job_menu` VALUES ('8', 'redis监控', '/job/redis', '0', '2018-12-20 23:02:02', '2018-12-20 23:02:25', '1', 'redis监控', '7', '2');
INSERT INTO `t_sys_job_menu` VALUES ('9', '执行器管理', '/job/jobgroup', '0', '2018-12-20 23:23:17', '2018-12-20 23:23:17', '1', '执行器管理', '3', '2');
INSERT INTO `t_sys_job_menu` VALUES ('10', '字典管理', '/job/dict', '0', '2018-12-20 23:24:23', '2018-12-20 23:24:23', '1', '字典管理', '1', '2');
INSERT INTO `t_sys_job_menu` VALUES ('11', '用户管理', '/job/user', '0', '2018-12-20 23:25:37', '2018-12-20 23:25:37', '1', '用户管理', '1', '2');
INSERT INTO `t_sys_job_menu` VALUES ('12', '角色管理', '/job/jobRole', '0', '2018-12-20 23:26:10', '2018-12-20 23:26:10', '1', '角色管理', '1', '2');
commit;
-- ---------------------------------------------用户----------------------------------------------
DROP TABLE IF EXISTS `t_sys_job_user`;
CREATE TABLE `t_sys_job_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) DEFAULT NULL COMMENT '账号',
  `password` varchar(32) DEFAULT NULL COMMENT '密码',
  `age` int(11) DEFAULT '18' COMMENT '年龄',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机',
  `address` varchar(50) DEFAULT NULL COMMENT '地址',
  `qq` varchar(12) DEFAULT NULL COMMENT 'QQ',
  `wechart` varchar(35) DEFAULT NULL COMMENT '微信号',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `flag` int(11) DEFAULT '-1' COMMENT '删除标记 1正常 -1删除',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `role_code` varchar(20) DEFAULT NULL COMMENT '角色代码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_phone` (`phone`) USING BTREE,
  KEY `idx_user_updatetime` (`UPDATE_TIME`) USING BTREE,
  KEY `idx_user_role` (`role_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='调度中心用户表';

-- ALTER TABLE `t_sys_job_user`
-- ADD UNIQUE INDEX `idx_user_phone` (`phone`) USING BTREE ,
-- ADD INDEX `idx_user_updatetime` (`update_time`) USING BTREE ,
-- ADD INDEX `idx_user_role` (`role_code`) USING BTREE ;
INSERT INTO `t_sys_job_user` VALUES ('1', 'admin', '30e229876358062f5d83bc824c81a99e', '18', '199199688@qq.com', '18610000006', null, null, null, '2018-12-19 22:17:19', '2018-12-24 00:22:16', '1', '222', 'admin_role');
commit;
-- ---------------------------------------------角色表----------------------------------------------
DROP TABLE IF EXISTS `t_sys_job_role`;
CREATE TABLE `t_sys_job_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(200) DEFAULT NULL COMMENT '角色名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `flag` int(1) DEFAULT '-1' COMMENT '删除标记 -1删除 1正常',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `role_code` varchar(20) DEFAULT NULL COMMENT '角色代码',
  PRIMARY KEY (`id`),
  KEY `idx_role_updatetime` (`update_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='角色表';
INSERT INTO `t_sys_job_role` VALUES ('19', '管理员', '2018-12-19 22:44:55', '2018-12-24 00:30:24', '1', '管理员', 'admin_role');
commit;
-- -------------------------------------------------菜单-角色关系表-------------------------------------------------------------------
DROP TABLE IF EXISTS `t_sys_job_menu_role`;
CREATE TABLE `t_sys_job_menu_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `flag` int(1) DEFAULT '1' COMMENT '删除标记 -1删除 1正常',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `role_code` varchar(20) DEFAULT NULL COMMENT '角色代码',
  PRIMARY KEY (`id`),
  KEY `idx_role_menu_id` (`menu_id`) USING BTREE,
  KEY `idx_role_role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8 COMMENT='菜单-角色关系表';

INSERT INTO `t_sys_job_menu_role` VALUES ('42', '19', '1', '2018-12-24 00:30:24', '2018-12-24 00:30:24', '1', null, 'admin_role');
INSERT INTO `t_sys_job_menu_role` VALUES ('43', '19', '12', '2018-12-24 00:30:24', '2018-12-24 00:30:24', '1', null, 'admin_role');
INSERT INTO `t_sys_job_menu_role` VALUES ('44', '19', '11', '2018-12-24 00:30:24', '2018-12-24 00:30:24', '1', null, 'admin_role');
INSERT INTO `t_sys_job_menu_role` VALUES ('45', '19', '10', '2018-12-24 00:30:24', '2018-12-24 00:30:24', '1', null, 'admin_role');
INSERT INTO `t_sys_job_menu_role` VALUES ('46', '19', '2', '2018-12-24 00:30:24', '2018-12-24 00:30:24', '1', null, 'admin_role');
INSERT INTO `t_sys_job_menu_role` VALUES ('47', '19', '7', '2018-12-24 00:30:24', '2018-12-24 00:30:24', '1', null, 'admin_role');
INSERT INTO `t_sys_job_menu_role` VALUES ('48', '19', '8', '2018-12-24 00:30:24', '2018-12-24 00:30:24', '1', null, 'admin_role');
INSERT INTO `t_sys_job_menu_role` VALUES ('49', '19', '3', '2018-12-24 00:30:24', '2018-12-24 00:30:24', '1', null, 'admin_role');
INSERT INTO `t_sys_job_menu_role` VALUES ('50', '19', '9', '2018-12-24 00:30:24', '2018-12-24 00:30:24', '1', null, 'admin_role');
INSERT INTO `t_sys_job_menu_role` VALUES ('51', '19', '6', '2018-12-24 00:30:24', '2018-12-24 00:30:24', '1', null, 'admin_role');
INSERT INTO `t_sys_job_menu_role` VALUES ('52', '19', '5', '2018-12-24 00:30:24', '2018-12-24 00:30:24', '1', null, 'admin_role');
INSERT INTO `t_sys_job_menu_role` VALUES ('53', '19', '4', '2018-12-24 00:30:24', '2018-12-24 00:30:24', '1', null, 'admin_role');
commit;