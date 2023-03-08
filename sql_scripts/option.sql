SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for vote_options
-- ----------------------------
DROP TABLE IF EXISTS `vote_option`;
CREATE TABLE `vote_option` (
  `option_id` char(19) NOT NULL COMMENT '选项id',
  `vote_id` char(19) NOT NULL COMMENT '题目id',
  `option_str` varchar(255) NOT NULL COMMENT '选项',
  `option_index` int(5) unsigned NOT NULL DEFAULT '0' COMMENT '是当前投票的第几个选项',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  `img_url` varchar(255) NOT NULL COMMENT '图片路径',
  `count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '选择当前选项的人数',
  PRIMARY KEY (`option_id`),
  KEY `idx_vote_id` (`vote_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='选项表';
