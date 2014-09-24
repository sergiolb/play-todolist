# --- Add task-user and dataset

# --- !Ups



create table taskUser (
  user                      varchar(255),
  name                      varchar(255) not null,
  constraint pk_taskUser primary key (user))
;


create sequence taskUser_seq start with 1000;
create sequence task_seq start with 1000;
alter table task add constraint fk_task_taskUser_1 foreign key (taskUser_user) references taskUser (user) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists taskUser;

drop table if exists task;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists taskUser_seq;

drop sequence if exists task_seq;