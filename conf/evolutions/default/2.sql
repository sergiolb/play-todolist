# --- Add task-user and dataset

# --- !Ups



create table taskUser (
  alias                      varchar(255) not null,
  name                      varchar(255),
  constraint pk_taskUser primary key (alias))
;


alter table task add alias varchar (255) not null;
alter table task add constraint fk_task_taskUser_1 foreign key (alias) references taskUser (alias) on delete restrict on update restrict;


insert into taskUser (alias,name) values ('Anonymous','');
insert into taskUser (alias,name) values ('Sergio','Sergio López');
insert into taskUser (alias,name) values ('Pablo','Pablo Gil');
insert into taskUser (alias,name) values ('Pedro','Pedro Paredes');

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

delete from taskUser 

drop table if exists taskUser;

drop table if exists task;

SET REFERENTIAL_INTEGRITY TRUE;

