DROP TABLE "db_option";
CREATE TABLE "db_option" (
  "db_id" NUMBER(20) VISIBLE NOT NULL ,
  "db_base" NUMBER(1) VISIBLE NOT NULL ,
  "db_name" VARCHAR2(256 BYTE) VISIBLE DEFAULT NULL ,
  "db_index_id" NUMBER(20) VISIBLE DEFAULT NULL ,
  "db_value_id" NUMBER(20) VISIBLE DEFAULT NULL
)
NOLOGGING
NOCOMPRESS
NOCACHE;

ALTER TABLE "db_option" ADD CHECK ("db_id" IS NOT NULL);
ALTER TABLE "db_option" ADD CHECK ("db_base" IS NOT NULL);

ALTER TABLE "db_option" ADD PRIMARY KEY ("db_id");

INSERT INTO "db_option" VALUES ('11', '1', 'ConfigStatus', NULL, NULL);
INSERT INTO "db_option" VALUES ('1', '1', 'Authroity', NULL, NULL);
INSERT INTO "db_option" VALUES ('2', '0', NULL, '1', '2');
INSERT INTO "db_option" VALUES ('3', '0', NULL, '12', '13');
INSERT INTO "db_option" VALUES ('4', '0', NULL, '31', '32');
INSERT INTO "db_option" VALUES ('5', '0', NULL, '41', '42');
INSERT INTO "db_option" VALUES ('6', '0', NULL, '11', '13');
INSERT INTO "db_option" VALUES ('7', '1', 'Boolean', NULL, NULL);
INSERT INTO "db_option" VALUES ('8', '1', 'Action', NULL, NULL);
INSERT INTO "db_option" VALUES ('9', '1', 'Dispose', NULL, NULL);
INSERT INTO "db_option" VALUES ('10', '1', 'FileStatus', NULL, NULL);
