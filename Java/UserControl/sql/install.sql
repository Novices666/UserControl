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

-- auto-generated definition
create table tag
(
    id         bigint auto_increment comment '标签id'
        primary key,
    tagName    varchar(255)                       not null comment '标签名',
    userId     bigint                             null comment '创建标签的用户id',
    parentId   bigint                             null comment '父级标签id',
    isParent   tinyint  default 0                 not null comment '是否为父级标签',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '逻辑删除标识',
    constraint index_tagName
        unique (tagName)
);

create index index_userId
    on tag (userId);

