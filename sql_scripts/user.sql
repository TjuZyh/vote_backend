SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` char(28) NOT NULL COMMENT '用户唯一id，对应微信的openid，固定为28位',
  `skey` varchar(100) NOT NULL COMMENT '自定义登录状态',
  `session_key` varchar(100) NOT NULL COMMENT 'session key',
  `avatar_url` varchar(255) DEFAULT NULL COMMENT '用户头像',
  `city` varchar(30) DEFAULT NULL COMMENT '用户所在城市',
  `last_visit_time` datetime NOT NULL COMMENT '最后登录时间',
  `country` varchar(30) DEFAULT NULL COMMENT '用户所在国家',
  `gender` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '男 1，女 0',
  `language` varchar(30) DEFAULT NULL COMMENT '用户使用语言',
  `nick_name` varchar(30) DEFAULT NULL COMMENT '用户昵称',
  `province` varchar(30) DEFAULT NULL COMMENT '用户所在省份',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  `type` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0普通用户 1管理员',
  `user_address` varchar(100) DEFAULT NULL COMMENT '用户生成的唯一钱包地址',
  `private_key` varchar(64) DEFAULT NULL COMMENT '用户私钥',
  `mnemonic` varchar(100) DEFAULT NULL COMMENT '助记词',
  `log_in` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '未登入 1，已登入 0',

  `real_name` varchar(30) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar (30) DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`user_id`),
  KEY `id_index` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';