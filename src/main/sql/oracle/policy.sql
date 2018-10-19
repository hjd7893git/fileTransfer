DROP TABLE "policy";
CREATE TABLE "policy" (
  "id" NUMBER(20) VISIBLE NOT NULL ,
  "name" VARCHAR2(256 BYTE) VISIBLE NOT NULL ,
  "nodeid" NUMBER(20) VISIBLE NOT NULL ,
  "path" VARCHAR2(1024 BYTE) VISIBLE NOT NULL ,
  "enc" NUMBER(1) VISIBLE NOT NULL ,
  "compress" NUMBER(1) VISIBLE NOT NULL ,
  "sign" NUMBER(1) VISIBLE NOT NULL ,
  "times" VARCHAR2(256 BYTE) VISIBLE NOT NULL ,
  "extensions" VARCHAR2(256 BYTE) VISIBLE NOT NULL
)
NOLOGGING
NOCOMPRESS
NOCACHE;

ALTER TABLE "policy" ADD CHECK ("id" IS NOT NULL);
ALTER TABLE "policy" ADD CHECK ("name" IS NOT NULL);
ALTER TABLE "policy" ADD CHECK ("nodeid" IS NOT NULL);
ALTER TABLE "policy" ADD CHECK ("path" IS NOT NULL);
ALTER TABLE "policy" ADD CHECK ("enc" IS NOT NULL);
ALTER TABLE "policy" ADD CHECK ("compress" IS NOT NULL);
ALTER TABLE "policy" ADD CHECK ("sign" IS NOT NULL);
ALTER TABLE "policy" ADD CHECK ("times" IS NOT NULL);
ALTER TABLE "policy" ADD CHECK ("extensions" IS NOT NULL);

ALTER TABLE "policy" ADD PRIMARY KEY ("id");

INSERT INTO "policy" VALUES ('1', 'policy01', '2', '/test1', '1', '1', '1', '*', '.doc,.txt,.docx');
INSERT INTO "policy" VALUES ('2', 'policy02', '2', '/test', '1', '1', '1', '12:30', '.tar,.zip,.tar.gz,.tgz,.jar');
INSERT INTO "policy" VALUES ('3', 'policy03', '2', '/test02', '0', '0', '0', '00:30,06:30,12:30,18:30', '*');
