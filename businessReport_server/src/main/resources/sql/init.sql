create table T_BUSINESS_RECORDINFO
(
  ID               VARCHAR2(60) not null
    primary key,
  UUID             VARCHAR2(255 char),
  RECORDENDTIME    DATE,
  RECORDSTARTTIME  DATE,
  CHANNELENDTIME   DATE,
  CHANNELSTARTTIME DATE,
  DURATION         NUMBER default 0,
  CALLNUMBER       VARCHAR2(255 char),
  ISCONNECT        VARCHAR2(255 char),
  PROTYPE          VARCHAR2(255 char),
  COLUMN1          VARCHAR2(255 char),
  COLUMN2          VARCHAR2(255 char),
  COLUMN3          VARCHAR2(255 char),
  COLUMN4          VARCHAR2(255 char),
  COLUMN5          VARCHAR2(255 char),
  COLUMN6          VARCHAR2(255 char),
  COLUMN7          VARCHAR2(255 char),
  COLUMN8          VARCHAR2(255 char),
  COLUMN9          VARCHAR2(255 char),
  COLUMN10         VARCHAR2(255 char),
  COLUMN11         VARCHAR2(255 char),
  COLUMN12         VARCHAR2(255 char),
  COLUMN13         VARCHAR2(255 char),
  COLUMN14         VARCHAR2(255 char),
  COLUMN15         VARCHAR2(255 char),
  COLUMN16         VARCHAR2(255 char),
  COLUMN17         VARCHAR2(255 char),
  COLUMN18         VARCHAR2(255 char),
  COLUMN19         VARCHAR2(255 char),
  COLUMN20         VARCHAR2(255 char),
  COLUMN21         VARCHAR2(255 char),
  COLUMN22         VARCHAR2(255 char),
  COLUMN23         VARCHAR2(255 char),
  COLUMN24         VARCHAR2(255 char),
  COLUMN25         VARCHAR2(255 char),
  COLUMN26         VARCHAR2(255 char),
  COLUMN27         VARCHAR2(255 char),
  COLUMN28         VARCHAR2(255 char),
  COLUMN29         VARCHAR2(255 char),
  COLUMN30         VARCHAR2(255 char),
  COLUMN31         VARCHAR2(255 char),
  COLUMN32         VARCHAR2(255 char),
  COLUMN33         VARCHAR2(255 char),
  COLUMN34         VARCHAR2(255 char),
  COLUMN35         VARCHAR2(255 char),
  COLUMN36         VARCHAR2(255 char),
  COLUMN37         VARCHAR2(255 char),
  COLUMN38         VARCHAR2(255 char),
  COLUMN39         VARCHAR2(255 char),
  COLUMN40         VARCHAR2(255 char),
  COLUMN41         VARCHAR2(255 char),
  COLUMN42         VARCHAR2(255 char),
  COLUMN43         VARCHAR2(255 char),
  COLUMN44         VARCHAR2(255 char),
  COLUMN45         VARCHAR2(255 char),
  COLUMN46         VARCHAR2(255 char),
  COLUMN47         VARCHAR2(255 char),
  COLUMN48         VARCHAR2(255 char),
  COLUMN49         VARCHAR2(255 char),
  COLUMN50         VARCHAR2(255 char),
  CONSTRAINT unique_key_uuid UNIQUE (UUID)
)
  PARTITION BY RANGE (RECORDSTARTTIME)
  INTERVAL (numtodsinterval(1, 'day'))
(
  PARTITION P1
    VALUES LESS THAN (TO_DATE('2016-10-01', 'YYYY-MM-DD'))
);
/
/

