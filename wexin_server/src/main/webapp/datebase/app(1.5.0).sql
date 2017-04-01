/*
SQLyog v10.2 
MySQL - 5.5.20 : Database - test
*********************************************************************
*/

CREATE TABLE `tb_innovation_skin` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `createdTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  `version` varchar(80) DEFAULT NULL COMMENT '版本',
  `url` varchar(80) DEFAULT NULL COMMENT '地址',
  `flag` int(10) DEFAULT NULL COMMENT '是否有效',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述',
  `size` varchar(80) DEFAULT NULL COMMENT '大小',
  `author` varchar(80) DEFAULT NULL COMMENT '资源包作者',
  `title` varchar(80) DEFAULT NULL COMMENT '主题',
  `type` varchar(80) DEFAULT NULL COMMENT '资源包类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_share_recode` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `userid` varchar(80) DEFAULT NULL COMMENT '用户id',
  `createdTime` datetime DEFAULT NULL COMMENT '创建时间',
  `content` varchar(999) DEFAULT NULL COMMENT '创建内容',
  `platform` varchar(80) DEFAULT NULL COMMENT '分享平台',
  `total` varchar(80) DEFAULT NULL COMMENT '分享目标',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `tb_news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(80) DEFAULT NULL COMMENT '标题',
  `abstract_txt` varchar(80) DEFAULT NULL COMMENT '描述',
  `pubDate` varchar(80) DEFAULT NULL COMMENT '日期',
  `source` varchar(80) DEFAULT NULL COMMENT '来源',
  `nid` varchar(80) DEFAULT NULL COMMENT '标示id',
  `url` varchar(80) DEFAULT NULL COMMENT '新闻Url',
  `channel_name` varchar(80) DEFAULT NULL COMMENT '类别名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

CREATE TABLE `tb_collection_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `report_id` varchar(80) DEFAULT NULL COMMENT '报告ID',
  `collection_id` varchar(80) DEFAULT NULL COMMENT '文摘ID',
  `owner_id` int(11) DEFAULT NULL COMMENT '收藏者ID',
  `coll_time` datetime DEFAULT NULL COMMENT '收藏时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间，一般是取消收藏时间',
  `state` int(1) DEFAULT '1' COMMENT '状态，1为正常，0为取消收藏',
  `new_id` varchar(80) DEFAULT NULL,
  `type` varchar(80) DEFAULT NULL COMMENT '类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=177 DEFAULT CHARSET=utf8 COMMENT='文摘-报告关联表';




