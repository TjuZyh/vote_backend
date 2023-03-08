SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for vote
-- ----------------------------
DROP TABLE IF EXISTS `votebox_time`;
CREATE TABLE `votebox_time` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增伪主键',
  `vote_id` char(19) NOT NULL COMMENT '投票id',
  `plan_start_time` datetime NOT NULL COMMENT '投票箱投票的计划开始时间',
  PRIMARY KEY (`id`),
  KEY `idx_vote_id` (`vote_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投票箱时间表';