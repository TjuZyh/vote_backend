SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for votedesc
-- ----------------------------
DROP TABLE IF EXISTS `votedesc`;
CREATE TABLE `votedesc` (
  `vote_id` char(19) NOT NULL COMMENT '对应投票id',
  `img_url` varchar(255) NOT NULL COMMENT '投票分享图片url',
  PRIMARY KEY (`vote_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投票图片描述表';