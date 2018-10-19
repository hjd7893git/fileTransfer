DROP TABLE "db_count";
CREATE TABLE "db_count" (
  "db_name" VARCHAR2(256 BYTE) VISIBLE NOT NULL ,
  "db_id" NUMBER(20) VISIBLE NOT NULL
)
NOLOGGING
NOCOMPRESS
NOCACHE;

ALTER TABLE "db_count" ADD CHECK ("db_name" IS NOT NULL);
ALTER TABLE "db_count" ADD CHECK ("db_id" IS NOT NULL);

ALTER TABLE "db_count" ADD PRIMARY KEY ("db_name");

INSERT INTO "db_count" VALUES ('menu', '9');
INSERT INTO "db_count" VALUES ('node', '3');
INSERT INTO "db_count" VALUES ('policy', '3');
INSERT INTO "db_count" VALUES ('policynode', '3');
INSERT INTO "db_count" VALUES ('user', '1');
INSERT INTO "db_count" VALUES ('usermenu', '9');
INSERT INTO "db_count" VALUES ('config', '0');
INSERT INTO "db_count" VALUES ('history', '0');
