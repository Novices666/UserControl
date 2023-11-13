-- auto-generated definition
create table user
(
    id           bigint auto_increment
        primary key,
    userAccount  varchar(255)                       null,
    userPassword varchar(512)                       not null,
    userType     int      default 0                 null comment '用户类型 0 - 普通用户 1 - 管理员',
    username     varchar(255)                       null,
    avatarUrl    varchar(1024)                      null,
    gender       tinyint                            null,
    phone        varchar(128)                       null,
    email        varchar(512)                       null,
    userStatus   int      default 0                 not null comment '0正常',
    createTime   datetime default CURRENT_TIMESTAMP null,
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete     tinyint  default 0                 not null
)
    engine = InnoDB;

