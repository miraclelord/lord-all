/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/4/4 18:00:09                            */
/*==============================================================*/


drop table if exists sys_config;

drop table if exists sys_dict;

drop table if exists sys_dict_group;

/*==============================================================*/
/* Table: sys_config                                            */
/*==============================================================*/
create table sys_config
(
   id                   bigint not null AUTO_INCREMENT comment '����Id',
   name                 varchar(40) not null comment '��������',
   config_key           varchar(100) not null comment '����key',
   config_value         varchar(500) comment '����value',
   order_value          bigint comment '����',
   parent_id            bigint comment '������',
   level                bit comment '�ȼ�',
   primary key (id)
);

alter table sys_config comment 'ϵͳ���ñ�';

/*==============================================================*/
/* Table: sys_dict                                              */
/*==============================================================*/
create table sys_dict
(
   id                   bigint not null AUTO_INCREMENT comment '����Id',
   name                 varchar(40) comment '����',
   dict_key             varchar(100) not null comment '�ֵ�key',
   dict_value           varchar(200) comment '�ֵ�value',
   group_id             bigint not null comment '����Id',
   dict_code            varchar(100) not null comment '����code',
   order_value          bigint comment '����',
   primary key (id)
);

alter table sys_dict comment 'ϵͳ�����ֵ��';

/*==============================================================*/
/* Table: sys_dict_group                                        */
/*==============================================================*/
create table sys_dict_group
(
   id                   bigint not null AUTO_INCREMENT comment '����Id',
   name                 varchar(40) not null comment '����',
   dict_code            varchar(100) not null comment '����code',
   order_value          bigint comment '����',
   primary key (id)
);

alter table sys_dict_group comment 'ϵͳ�����ֵ�����';

