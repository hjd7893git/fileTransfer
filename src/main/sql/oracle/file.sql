DROP TABLE "file";
CREATE TABLE "file" (
  "name" VARCHAR2(256 BYTE) VISIBLE NOT NULL ,
  "nodeid" NUMBER(20) VISIBLE NOT NULL ,
  "policyid" NUMBER(20) VISIBLE NOT NULL ,
  "hash" VARCHAR2(256 BYTE) VISIBLE NOT NULL ,
  "size" NUMBER(20) VISIBLE NOT NULL ,
  "progress" NUMBER(20) VISIBLE NOT NULL ,
  "starttime" VARCHAR2(256 BYTE) VISIBLE NOT NULL ,
  "endtime" VARCHAR2(256 BYTE) VISIBLE ,
  "status" VARCHAR2(256 BYTE) VISIBLE NOT NULL
)
NOLOGGING
NOCOMPRESS
NOCACHE;

ALTER TABLE "file" ADD CHECK ("name" IS NOT NULL);
ALTER TABLE "file" ADD CHECK ("nodeid" IS NOT NULL);
ALTER TABLE "file" ADD CHECK ("policyid" IS NOT NULL);
ALTER TABLE "file" ADD CHECK ("hash" IS NOT NULL);
ALTER TABLE "file" ADD CHECK ("size" IS NOT NULL);
ALTER TABLE "file" ADD CHECK ("progress" IS NOT NULL);
ALTER TABLE "file" ADD CHECK ("starttime" IS NOT NULL);
ALTER TABLE "file" ADD CHECK ("status" IS NOT NULL);

ALTER TABLE "file" ADD PRIMARY KEY("name", "nodeid", "policyid", "hash");

INSERT INTO "file" VALUES ('123.tar.gz', '1', '1', '222222', '100', '100', '2018-04-15 07:00:00', '2018-04-15 07:00:05', 'success');
INSERT INTO "file" VALUES ('123.tar.gz', '1', '2', '222222', '100', '100', '2018-04-15 07:00:00', '2018-04-15 07:00:05', 'success');
INSERT INTO "file" VALUES ('123.tar.gz', '1', '3', '222222', '100', '100', '2018-04-15 07:00:00', '2018-04-15 07:00:05', 'success');
INSERT INTO "file" VALUES ('123.tar', '1', '1', '111111', '100', '100', '2018-04-13 07:08:09', '2018-04-13 08:00:01', 'success');
INSERT INTO "file" VALUES ('123.tar', '1', '2', '111111', '100', '100', '2018-04-13 09:00:00', '2018-04-13 09:00:05', 'success');
INSERT INTO "file" VALUES ('123.tar.gz', '1', '1', '333333', '100', '100', '2018-04-16 07:00:00', '2018-04-16 07:00:05', 'success');
INSERT INTO "file" VALUES ('123.tar.gz', '1', '2', '333333', '100', '100', '2018-04-16 07:00:00', '2018-04-16 07:00:05', 'success');
INSERT INTO "file" VALUES ('123.tar.gz', '1', '3', '333333', '100', '100', '2018-04-16 07:00:00', NULL, 'sending');
INSERT INTO "file" VALUES ('aaa.zip', '1', '1', '112311', '50', '100', '2018-03-04 05:30:54', '2018-03-04', 'success');
INSERT INTO "file" VALUES ('aaa.zip', '1', '2', '112311', '50', '1', '2018-04-10 06:30:51', NULL, 'sending');
INSERT INTO "file" VALUES ('aaa.zip', '1', '3', '112311', '50', '1', '2018-04-10 06:30:51', NULL, 'failure');
INSERT INTO "file" VALUES ('123.tar', '1', '3', '111111', '100', '100', '2018-04-13 09:00:00', NULL, 'success');
