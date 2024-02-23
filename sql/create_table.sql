-- auto-generated definition
create table user
(
    id            bigint auto_increment comment '用户ID'
        primary key,
    user_account  varchar(256)                                                                                                             null comment '用户账户',
    nickname      varchar(256)  default '匿名用户'                                                                                         null comment '用户昵称',
    avatar_url    varchar(1024) default 'https://edu-2022-10.oss-cn-shanghai.aliyuncs.com/2022/10/03/02905917b196d71be000265e363abd8e.jpg' null comment '用户头像',
    gender        tinyint                                                                                                                  null comment '性别',
    user_password varchar(256)                                                                                                             null comment '用户密码',
    phone         varchar(256)                                                                                                             null comment '电话',
    email         varchar(256)                                                                                                             null comment '邮箱',
    user_status   int           default 0                                                                                                  null comment '用户状态 0-正常',
    create_time   datetime      default CURRENT_TIMESTAMP                                                                                  not null comment '创建时间',
    update_time   datetime      default CURRENT_TIMESTAMP                                                                                  not null comment '更新时间',
    is_delete     tinyint       default 0                                                                                                  not null comment '是否删除 0 1（逻辑删除）',
    user_role     int           default 0                                                                                                  null comment '用户角色 0-普通用户，1-管理员',
    auth_code     varchar(512)                                                                                                             not null comment '授权码'
)
    comment '用户';