# --- Add task-user and dataset

# --- !Ups



create table taskUser (
  user                      varchar(255),
  name                      varchar(255) not null,
  constraint pk_taskUser primary key (user))
;


alter table task add user varchar (255) not null;
alter table task add constraint fk_task_taskUser_1 foreign key (user) references taskUser (user) on delete restrict on update restrict;


insert into taskUser (user,name) values (  'Anonymous','');
insert into taskUser (user,name) values (  'Sergio','Sergio López');
insert into taskUser (user,name) values (  'Pablo','Pablo Gil');
insert into taskUser (user,name) values (  'Pedro','Pedro Paredes');

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

delete from taskUser 

drop table if exists taskUser;

drop table if exists task;

SET REFERENTIAL_INTEGRITY TRUE;

