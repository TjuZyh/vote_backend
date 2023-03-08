SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for voteboxuser
-- ----------------------------
DROP TABLE IF EXISTS `voteboxuser`;
CREATE TABLE `voteboxuser` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增伪主键',
  `vote_id` char(19) NOT NULL COMMENT '投票id',
  `user_id` char(28) NOT NULL COMMENT '投票人id',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;