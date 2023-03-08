SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for vote_user
-- ----------------------------
DROP TABLE IF EXISTS `vote_user`;
CREATE TABLE `vote_user` (
  `id` char(19) NOT NULL COMMENT '自动分配的主键',
  `user_id` char(28) NOT NULL,
  `vote_id` char(19) NOT NULL,
  `option_id` char(19) DEFAULT NULL,
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',

  `real_name` varchar(30) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar (30) DEFAULT NULL COMMENT '手机号',
  `transaction_id` char(100) DEFAULT NULL COMMENT '交易id',

  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_vote_id` (`vote_id`),
  KEY `idx_option_id` (`option_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;