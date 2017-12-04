CREATE TABLE `message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '标题',
  `read_time` datetime DEFAULT NULL,
  `subtitle` varchar(200) NOT NULL DEFAULT '' COMMENT '副标题',
  `content` text COMMENT '内容',
  `status` varchar(10) NOT NULL DEFAULT 'INIT' COMMENT '阅读状态',
  `source` varchar(20) NOT NULL DEFAULT '' COMMENT '从某个系统发出',
  `target` bigint(20) NOT NULL COMMENT '发送给某个用户',
  `publish_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_delete` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;