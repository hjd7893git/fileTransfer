DROP TABLE "db_relation";
CREATE TABLE "db_relation" (
  "db_id" NUMBER(20) VISIBLE NOT NULL ,
  "db_masterfieldid" NUMBER(20) VISIBLE NOT NULL ,
  "db_slaverfieldid" NUMBER(20) VISIBLE NOT NULL ,
  "db_update" NUMBER(1) VISIBLE NOT NULL ,
  "db_delete" NUMBER(1) VISIBLE NOT NULL
)
NOLOGGING
NOCOMPRESS
NOCACHE;

ALTER TABLE "db_relation" ADD CHECK ("db_id" IS NOT NULL);
ALTER TABLE "db_relation" ADD CHECK ("db_masterfieldid" IS NOT NULL);
ALTER TABLE "db_relation" ADD CHECK ("db_slaverfieldid" IS NOT NULL);
ALTER TABLE "db_relation" ADD CHECK ("db_update" IS NOT NULL);
ALTER TABLE "db_relation" ADD CHECK ("db_delete" IS NOT NULL);

ALTER TABLE "db_relation" ADD PRIMARY KEY ("db_id");

INSERT INTO "db_relation" VALUES ('1', '1', '12', '0', '1');
INSERT INTO "db_relation" VALUES ('2', '6', '13', '0', '1');
