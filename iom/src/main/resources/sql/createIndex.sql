-- auto-generated definition
create index PROJECTINDEX
    on T_IOM_RECORDINFO (PROJECTID)
/
-- auto-generated definition
create index RULEINDEX
    on T_IOM_RECORDINFO (RULEID)
/

-- auto-generated definition
create index TASKINDEX
    on T_IOM_RECORDINFO (TASKID)
/

-- auto-generated definition
create index FLOWINDEX
    on T_IOM_RECORDINFO (FLOWID)
/

create index TIMEINDEX
    on T_IOM_RECORDINFO (RECORDSTARTTIME)
/

create index UUIDNDEX
    on T_IOM_RECORDINFO (UUID)
/

-- auto-generated definition
create index TYPEINDEX
    on T_IOM_CALLCONTENT (TYPE)
/

-- auto-generated definition
create index CONTENTUUIDINDEX
    on T_IOM_CALLCONTENT (UUID)
/

-- auto-generated definition
create index CONTENTDETAILUUIDINDEX
    on T_IOM_CALLCONTENTDETAIL (UUID)
/

-- auto-generated definition
create index RESINFOUUIDINDEX
    on T_IOM_RESINFO (UUID)
/

-- auto-generated definition
create index CUSTOMERINFOUUIDINDEX
    on T_IOM_CUSTOMERINFO (UUID)
/

-- auto-generated definition
create index THROUGHUUIDINDEX
    on T_IOM_SCENETHROUGHDETAIL (UUID)
/


-- auto-generated definition
create index STATUSTICSPROINDEX
    on T_IOM_CALLSTATISTICS (PROJECTID)
/


create index STATUSTICSRULEINDEX
    on T_IOM_CALLSTATISTICS (RULEID)
/


create index STATUSTICSTASKINDEX
    on T_IOM_CALLSTATISTICS (TASKID)
/


create index INFOREFLEXPROINDEX
    on T_IOM_INFOREFLEX (PROJECTID)
/



create index QUALITYPROINDEX
    on T_IOM_QUALITY (PROJECTID)
/


create index INFOTAGUUIDINDEX
    on T_IOM_RECORDINFOTAG (UUID)
/


--增加唯一约束
--alter table T_IOM_RECORDINFO
--    add constraint unique_key_uuid
--        unique (UUID);
