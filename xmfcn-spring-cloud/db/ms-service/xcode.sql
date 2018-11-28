
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='sss';

-- ----------------------------
-- Records of t_base_user
-- ----------------------------
INSERT INTO `t_base_user` VALUES ('1', 'admin', 'e99a18c428cb38d5f260853678922e03', '18', '199199688@qq.com', '', null, null, '', '2017-03-13 16:07:38', '2017-11-08 16:09:38', '0', '', '1');



