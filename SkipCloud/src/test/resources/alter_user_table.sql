alter table user
add full_name varchar(20) NOT NULL DEFAULT 'Tom' COMMENT '姓名',
add email varchar(20) NOT NULL DEFAULT '85025@qq.com' COMMENT '邮箱',
add address varchar(50) NOT NULL DEFAULT '深圳南山区白石洲' COMMENT '地址',
add city varchar(20) NOT NULL DEFAULT '深圳' COMMENT '城市',
add country varchar(20) NOT NULL DEFAULT 'China' COMMENT '国家';