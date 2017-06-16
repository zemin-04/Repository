/*
SQLyog 企业版 - MySQL GUI v8.14
MySQL - 5.5.27 : Database - skipcloud
*********************************************************************
*/

/*Table structure for table 'permission' */

DROP TABLE IF EXISTS permission;

CREATE TABLE permission(
  id bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '权限id',
  permission_name varchar(32) DEFAULT NULL COMMENT '权限名',
  permission_sign varchar(128) DEFAULT NULL COMMENT '权限标识,程序中判断使用,如"user:create"',
  description varchar(256) DEFAULT NULL COMMENT '权限描述,UI界面显示使用',
  PRIMARY KEY (id)
) COMMENT='权限表';

/*Data for the table 'permission' */

insert  into permission(id,permission_name,permission_sign,description) values (1,'用户新增','user:create','新增用户');

/*Table structure for table 'role' */

DROP TABLE IF EXISTS role;

CREATE TABLE role(
  id bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '角色id',
  role_name varchar(32) DEFAULT NULL COMMENT '角色名',
  role_sign varchar(128) DEFAULT NULL COMMENT '角色标识,程序中判断使用,如"admin"',
  description varchar(256) DEFAULT NULL COMMENT '角色描述,UI界面显示使用',
  PRIMARY KEY (id)
) COMMENT='角色表';

/*Data for the table 'role' */

insert  into role(id,role_name,role_sign,description) values (1,'admin','admin','管理员');

/*Table structure for table 'role_permission' */

DROP TABLE IF EXISTS role_permission;

CREATE TABLE role_permission(
  id bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '表id',
  role_id bigint(20) unsigned DEFAULT NULL COMMENT '角色id',
  permission_id bigint(20) unsigned DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (id)
) COMMENT='角色与权限关联表';

/*Data for the table 'role_permission' */

insert  into role_permission(id,role_id,permission_id) values (1,2,1);

/*Table structure for table 'user' */

DROP TABLE IF EXISTS user;

CREATE TABLE user(
  id bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户id',
  username varchar(50) DEFAULT NULL COMMENT '用户名',
  password char(64) DEFAULT NULL COMMENT '密码',
  state varchar(32) DEFAULT NULL COMMENT '状态',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (id)
) COMMENT='用户表';

/*Data for the table 'user' */

insert  into user(id,username,password,state,create_time) values (1,'zemin','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92','正常','2014-07-17 12:59:08');

/*Table structure for table 'user_role' */

DROP TABLE IF EXISTS user_role;

CREATE TABLE user_role(
  id bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '表id',
  user_id bigint(20) unsigned DEFAULT NULL COMMENT '用户id',
  role_id bigint(20) unsigned DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (id)
) COMMENT='用户与角色关联表';

/*Data for the table 'user_role' */

insert  into user_role(id,user_id,role_id) values (1,1,1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
