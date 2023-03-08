SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for vote
-- ----------------------------
DROP TABLE IF EXISTS `vote`;
CREATE TABLE `vote` (
  `vote_id` char(19) NOT NULL COMMENT '当前投票id',
  `user_id` char(28) NOT NULL COMMENT '用户id',
  `title` varchar(30) DEFAULT NULL COMMENT '投票标题',
  `vote_desc` varchar(100) DEFAULT NULL COMMENT '投票描述',
  `type` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0单选题 1多选',
  `is_anonymous` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0 允许匿名 1不允许匿名',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  `sum_user` bigint(10) unsigned NOT NULL DEFAULT '0' COMMENT '统计人数',
  `sum_vote` bigint(10) unsigned NOT NULL DEFAULT '0' COMMENT '统计票数',
  `end_date` datetime NOT NULL COMMENT '截止时间',

  `vote_type` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0一般群投票 1有图群投票 2投票箱投票',
  `shuffle_options` tinyint(1) unsigned NOT NULL DEFAULT  '0' COMMENT '0不随机重排选项 1随机重排选项',
  `daily_vote` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0仅一次投票 1每日投票',
  `need_personal_information` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0不需要个人信息 1需要个人信息',
  `display_option` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0显示票数 1显示票数和明细 2不显示票数',
  `img_url` varchar(255) DEFAULT NULL COMMENT '投票分享图片url',
  `transaction_id` char(100) DEFAULT NULL COMMENT '交易id',
  `only_group_member` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0 允许非群成员投票 1 不允许非群成员投票',
  `least_limit` tinyint(1) unsigned DEFAULT '0' COMMENT '至少可选',
  `most_limit` tinyint(1) unsigned DEFAULT '0' COMMENT '至多可选',
  PRIMARY KEY (`vote_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投票表';