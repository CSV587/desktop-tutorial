create table T_BUSINESS_CALLBACK
(
    ID                    VARCHAR2(60) not null
        primary key,
    CALLTASKID            VARCHAR2(255 char),
    CALLTASKFISCID        VARCHAR2(255 char),
    CALLOUTTYPE1          VARCHAR2(255 char),
    CALLOUTTYPE2          VARCHAR2(255 char),
    POLICYNO              VARCHAR2(255 char),
    APPLICANTNAME         VARCHAR2(255 char),
    APPLICANTSEX          VARCHAR2(255 char),
    RISKNAME              VARCHAR2(2000 char),
    EFFECTDT              VARCHAR2(255 char),
    NEXTDEDUCTDT          VARCHAR2(255 char),
    PAYABLEDT             VARCHAR2(255 char),
    RENEWALPAYABLEPREM    VARCHAR2(255 char),
    LASTDEDUCTDT          VARCHAR2(255 char),
    DEDUCTACCOUNT         VARCHAR2(255 char),
    DEDUCTACCOUNTCODE     VARCHAR2(255 char),
    GRACEPERIODEXPIRYDT   VARCHAR2(255 char),
    APPLICANTBIRTHDAY     VARCHAR2(255 char),
    APPLICANTCDTP         VARCHAR2(255 char),
    APPLICANTCDNO         VARCHAR2(255 char),
    PAYPERIOND            VARCHAR2(255 char),
    DEDUCTERRNOTE         VARCHAR2(255 char),
    DEDUCTBANKNAME        VARCHAR2(255 char),
    PHONES                VARCHAR2(255 char),
    POLICYSTATE           VARCHAR2(255 char),
    CUSTSAMEFLAG          VARCHAR2(255 char),
    POLICYOVERDUESTATE    VARCHAR2(255 char),
    INSUREDNAME           VARCHAR2(255 char),
    LASTCOMPLEXAPPLYDT    VARCHAR2(255 char),
    INSUREDBIRTHDAY       VARCHAR2(255 char),
    BROADCASTPOLICYSTATE  VARCHAR2(255 char),
    POLICYACQUISITIONDATE VARCHAR2(255 char),
    IMPORTDATE            date,
    DISTRIBUTEDATE        date,
    DISTRIBUTEFLAG        NUMBER default 0,
    CLOSEDATE             date,
    CLOSEFLAG             NUMBER default 0,
    CALLCOUNT             NUMBER default 0,
    YEAR                  VARCHAR2(20 char),
    CREATETIME            date
);
/

create table T_BUSINESS_PAPER
(
  id         NVARCHAR2(255) not null,
  sort       NVARCHAR2(255),
  seq        NUMBER default 0,
  loid       NVARCHAR2(255),
  createdate DATE,
  name       NVARCHAR2(255),
  projectid  NVARCHAR2(255),
  flowid     NVARCHAR2(255)
);
alter table T_BUSINESS_PAPER
  add constraint T_UNIQUE_PAPER_SORT unique (SORT);

create table T_BUSINESS_QUESTION
(
  id           NVARCHAR2(255) not null,
  questionid   NVARCHAR2(255),
  loid         NVARCHAR2(255),
  createdate   DATE default sysdate,
  paperid      NVARCHAR2(255),
  identifier   NUMBER default 1
);
alter table T_BUSINESS_QUESTION
  add constraint T_UNIQUE_QUESTION_QID unique (QUESTIONID);
alter table T_BUSINESS_QUESTION
  add constraint T_FK_QUESTION_QID foreign key (PAPERID)
  references T_BUSINESS_PAPER (SORT) on delete cascade;
/

create table T_BUSINESS_CHILDQUESTION
(
  childid      NVARCHAR2(255) not null,
  childname    NVARCHAR2(255),
  subjectivity CLOB,
  identifier   NUMBER default 1,
  condition    CLOB,
  seq          NUMBER default 1,
  parentid     NVARCHAR2(255)
);
alter table T_BUSINESS_CHILDQUESTION
  add constraint T_FK_CHILDQUESTION_PARENTID foreign key (PARENTID)
  references T_BUSINESS_QUESTION (QUESTIONID) on delete cascade;
/

create table T_BUSINESS_CALLCYCLEMANAGE
(
  id             NVARCHAR2(255) not null,
  pushtaskid     NVARCHAR2(255),
  returntaskid   NVARCHAR2(255),
  tasktotal      NUMBER,
  taskstate      NUMBER,
  startdate      DATE,
  enddate        DATE,
  newstartdate   DATE,
  newenddate     DATE,
  editor         NVARCHAR2(255),
  editdate       DATE,
  callstate      NUMBER
);
alter table T_BUSINESS_CALLCYCLEMANAGE
  add constraint ID unique (ID);