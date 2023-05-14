drop table members if exists cascade;
create table members (
 member_id varchar(10),
 money integer not null default 0,
 primary key (member_id)
);
insert into members(member_id, money) values ('hi1',10000);
insert into members(member_id, money) values ('hi2',20000);