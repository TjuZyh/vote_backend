SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for blockchaininfo
-- ----------------------------
DROP TABLE IF EXISTS `vote_block_chain`;
CREATE TABLE `vote_block_chain` (
  `transaction_id` char(100) NOT NULL COMMENT '交易id',
  `block_hash` char(100) NOT NULL COMMENT '块hash',
  `chain_status` char(10) NOT NULL COMMENT '链状态',
  `block_height` bigint(10) NOT NULL COMMENT'块高',
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='区块链表';