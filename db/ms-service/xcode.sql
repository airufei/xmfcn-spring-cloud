
-- ----------------------------
-- Table structure for `t_base_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_base_user`;
CREATE TABLE `t_base_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL COMMENT '账号',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `age` int(11) DEFAULT '18' COMMENT '年龄',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机',
  `address` varchar(50) DEFAULT NULL COMMENT '地址',
  `qq` varchar(12) DEFAULT NULL COMMENT 'QQ',
  `wechart` varchar(35) DEFAULT NULL COMMENT '微信号',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `flag` int(11) DEFAULT '-1' COMMENT '删除标记 1正常 -1删除',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `type` int(1) unsigned zerofill DEFAULT '0' COMMENT '用户类型: 0是互联网用户，1是管理员用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of t_base_user
-- ----------------------------
INSERT INTO `t_base_user` VALUES ('1', 'admin', 'e99a18c428cb38d5f260853678922e03', '18', '199199688@qq.com', '', null, null, '', '2017-03-13 16:07:38', '2017-11-08 16:09:38', '0', '', '1');
-- ----------------------------
-- 字典表
-- ----------------------------
CREATE TABLE `t_sys_dict` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `DICT_KEY` varchar(200) DEFAULT NULL COMMENT '值',
  `DICT_VALUE` text COMMENT '名称',
  `FID` bigint(20) DEFAULT NULL COMMENT '父级ID',
  `TYPE` varchar(40) DEFAULT NULL COMMENT '类型',
  `FLAG` int(11) DEFAULT '1' COMMENT '删除标记 -1删除 1正常',
  `REMARK` text COMMENT '备注',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `SORT` int(10) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=499 DEFAULT CHARSET=utf8 COMMENT='字典数据表';

-- ----------------------------
-- 微信用户表
-- ----------------------------
CREATE TABLE `t_wx_user` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `nickName` varchar(100) DEFAULT NULL COMMENT '昵称',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `country` varchar(50) DEFAULT NULL COMMENT '城市',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '修改时间',
  `flag` int(11) DEFAULT '0' COMMENT '删除标记',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `photoUrl` varchar(500) DEFAULT NULL COMMENT '头像url',
  `openId` varchar(64) DEFAULT NULL COMMENT '微信用户id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_wx_user_openId` (`openId`) USING BTREE,
  KEY `idx_wx_user_updatetime` (`updatetime`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COMMENT='微信用户表';

-- ----------------------------
-- 微信照片信息表
-- ----------------------------
CREATE TABLE `t_wx_photo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '图片名称',
  `type` varchar(50) DEFAULT NULL COMMENT '图片类型',
  `url` varchar(50) DEFAULT NULL COMMENT '图片地址',
  `description` varchar(50) DEFAULT NULL COMMENT '图片描述',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '修改时间',
  `flag` int(11) DEFAULT '0' COMMENT '删除标记',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `path` varchar(255) DEFAULT NULL COMMENT '存储地址',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_wx_phone'_type` (`type`) USING BTREE,
  KEY `idx_wx_user_updatetime` (`updatetime`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COMMENT='微信照片信息表';
-- ----------------------------
-- 微信点赞
-- ----------------------------
CREATE TABLE `t_wx_user_like` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `openid` varchar(100) DEFAULT NULL COMMENT '用户唯一标识',
  `type` varchar(50) DEFAULT NULL COMMENT '类型',
  `photourl` varchar(255) DEFAULT NULL COMMENT '头像',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '修改时间',
  `flag` int(11) DEFAULT '0' COMMENT '删除标记',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `nickname` varchar(100) DEFAULT NULL COMMENT '昵称',
  `bizid` varchar(64) DEFAULT NULL COMMENT '业务ID',
  PRIMARY KEY (`id`),
  KEY `idx_wx_like_openid` (`openid`) USING BTREE,
  KEY `idx_wx_like_type` (`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COMMENT='微信点赞';

-- ----------------------------
-- 微信留言
-- ----------------------------
CREATE TABLE `t_wx_user_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `openid` varchar(100) DEFAULT NULL COMMENT '用户唯一标识',
  `type` varchar(50) DEFAULT NULL COMMENT '类型',
  `content` varchar(200) DEFAULT NULL COMMENT '内容',
  `photourl` varchar(255) DEFAULT NULL COMMENT '头像',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '修改时间',
  `flag` int(11) DEFAULT '0' COMMENT '删除标记',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `nickname` varchar(100) DEFAULT NULL COMMENT '昵称',
  `bizid` varchar(64) DEFAULT NULL COMMENT '业务ID',
  PRIMARY KEY (`id`),
  KEY `idx_wx_user_openid` (`openid`) USING BTREE,
  KEY `idx_wx_msg_type` (`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COMMENT='微信留言';

