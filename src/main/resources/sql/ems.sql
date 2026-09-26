create table biz_customer
(
    id             int unsigned auto_increment comment '自增id'
        primary key,
    name           varchar(10)  not null comment '客户名',
    contact_person varchar(10)  null comment '联系人',
    phone          varchar(20)  null comment '联系电话',
    address        varchar(100) null comment '客户地址',
    create_time    datetime     null comment '创建时间',
    update_time    datetime     null comment '更新时间',
    constraint biz_customer_pk
        unique (name)
)
    comment '存储客户信息';

create table biz_process
(
    id          int unsigned auto_increment comment '自增id'
        primary key,
    name        varchar(10)  not null comment '工艺名',
    remark      varchar(255) null comment '备注/简介',
    create_time datetime     null comment '创建时间',
    update_time datetime     null comment '更新时间',
    constraint biz_process_pk
        unique (name)
)
    comment '工艺表（加工类型）';

create table biz_customer_process_price
(
    id              int unsigned auto_increment comment '自增id'
        primary key,
    customer_id     int unsigned  not null comment '客户id',
    process_id      int unsigned  not null comment '工艺id',
    real_unit_price decimal(4, 3) not null comment '真单价',
    fake_unit_price decimal(4, 3) not null comment '假单价，用于前端展示',
    constraint biz_customer_process_price_biz_customer_id_fk
        foreign key (customer_id) references biz_customer (id),
    constraint biz_customer_process_price_biz_process_id_fk
        foreign key (process_id) references biz_process (id)
)
    comment '客户工艺单价表';

create table biz_order
(
    id                 int unsigned auto_increment comment '自增id'
        primary key,
    customer_id        int unsigned             not null comment '客户id',
    process_id         int unsigned             not null comment '工艺id',
    order_date         date                     not null comment '订单日期',
    product_name       varchar(50)              not null comment '产品名称',
    spec_1             decimal(4, 1)            not null comment '规格1',
    spec_2             decimal(4, 1)            not null comment '规格2',
    special_unit_price decimal(4, 3)            null comment '特殊单价',
    quantity           int unsigned             not null comment '数量',
    amount             decimal(10, 2)           not null comment '总价',
    remark             varchar(255)             null comment '备注',
    is_deleted         tinyint      default 0   not null comment '已删除，1：已删除，0：未删除',
    print_count        int unsigned default '0' not null comment '打印次数',
    create_time        datetime                 null comment '创建时间',
    update_time        datetime                 null comment '修改时间',
    constraint biz_order_biz_customer_id_fk
        foreign key (customer_id) references biz_customer (id),
    constraint biz_order_biz_process_id_fk
        foreign key (process_id) references biz_process (id)
)
    comment '正式订单表';

create table biz_prepare_order
(
    id                 int unsigned auto_increment comment '自增id'
        primary key,
    customer_id        int unsigned  not null comment '客户id',
    process_id         int unsigned  not null comment '工艺id',
    order_date         date          not null comment '订单日期',
    product_name       varchar(50)   null comment '产品名称',
    spec_1             decimal(4, 1) null comment '规格1',
    spec_2             decimal(4, 1) null comment '规格2',
    special_unit_price decimal(4, 3) null comment '特殊单价，有则以此为准',
    remark             varchar(50)   null comment '备注',
    create_time        datetime      null comment '创建时间',
    update_time        datetime      null comment '修改时间',
    constraint biz_prepare_order_biz_customer_id_fk
        foreign key (customer_id) references biz_customer (id),
    constraint biz_prepare_order_biz_process_id_fk
        foreign key (process_id) references biz_process (id)
)
    comment '预备订单';

create table sys_update_log
(
    id              int unsigned auto_increment comment '自增id'
        primary key,
    version         varchar(10) not null comment '版本号',
    update_date     date        not null,
    update_contents json        not null comment '更新内容',
    create_time     datetime    null comment '创建时间',
    update_time     datetime    null comment '更新时间'
);

create table sys_user
(
    id          int unsigned auto_increment comment '自增id'
        primary key,
    username    varchar(10)       not null comment '用户名',
    account     varchar(30)       not null comment '账号',
    password    varchar(100)      not null comment '密码',
    role        tinyint default 1 not null comment '类别 1:用户 2:管理员',
    create_time datetime          null comment '创建时间',
    update_time date              null comment '更新时间',
    constraint sys_user_pk
        unique (username),
    constraint sys_user_pk_2
        unique (account)
);

create table sys_operation_log
(
    id          int unsigned auto_increment comment '自增id'
        primary key,
    user_id     int unsigned null comment '用户id',
    operation   varchar(100) null comment '操作描述',
    method      varchar(200) null comment 'Controller方法',
    params      text         null comment '请求参数JSON',
    ip          varchar(50)  null comment '客户端ip',
    status      tinyint      null comment '1成功 0失败',
    error_msg   varchar(500) null comment '失败原因',
    cost_time   bigint       null comment '耗时ms',
    create_time datetime     null comment '创建时间',
    constraint sys_operation_log_sys_user_id_fk
        foreign key (user_id) references sys_user (id)
)
    comment '操作日志表';


