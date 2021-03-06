create table JK_WF_ROLE 
(
   ID                   CHAR(32)            not null,
   ROLE_FLAG            VARCHAR2(6),
   ROLE_NAME            VARCHAR2(64),
   ROLE_DESC            VARCHAR2(128),
   constraint PK_JK_WF_ROLE primary key (ID)
);

comment on table JK_WF_ROLE is
'流程角色';

comment on column JK_WF_ROLE.ROLE_NAME is
'角色名称';

comment on column JK_WF_ROLE.ROLE_DESC is
'角色描述';

--===========================================================--
create table JK_WF_ROLE_USER_RELATION 
(
   ID                   CHAR(32)             not null,
   ROLE_ID              CHAR(32),
   USER_ID              VARCHAR2(10),
   constraint PK_JK_WF_ROLE_USER_RELATION primary key (ID)
);

comment on table JK_WF_ROLE_USER_RELATION is
'流程角色与人员关系表';

comment on column JK_WF_ROLE_USER_RELATION.ROLE_ID is
'角色ID';

comment on column JK_WF_ROLE_USER_RELATION.USER_ID is
'人员ID';

-----------------------------------------------------------------
create table JK_WF_TASK
(
	ID				char(32)		not null,
	BUSI_TYPE		varchar2(20),
	TASK_ID			varchar2(64),
	PROC_INST_ID	varchar2(64),
	URL_			varchar2(512),
	CUR_USER		INTEGER,
	CUR_TIME		TIMESTAMP,
	SUBMIT_USER		INTEGER,
	SUBMIT_DEPT		INTEGER,
	APPLY_ID		INTEGER,
	TASK_STATE		INTEGER,
	CREATE_USER		INTEGER,
	constraint JK_WF_TASK primary key (ID)
);



