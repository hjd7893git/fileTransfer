DROP TABLE "usermenu";
CREATE TABLE "usermenu" (
  "id" NUMBER(20) VISIBLE NOT NULL ,
  "userid" NUMBER(20) VISIBLE NOT NULL ,
  "menuid" NUMBER(20) VISIBLE NOT NULL ,
  "type" VARCHAR2(256 BYTE) VISIBLE NOT NULL
)
NOLOGGING
NOCOMPRESS
NOCACHE;

ALTER TABLE "usermenu" ADD CHECK ("id" IS NOT NULL);
ALTER TABLE "usermenu" ADD CHECK ("userid" IS NOT NULL);
ALTER TABLE "usermenu" ADD CHECK ("menuid" IS NOT NULL);
ALTER TABLE "usermenu" ADD CHECK ("type" IS NOT NULL);

ALTER TABLE "usermenu" ADD PRIMARY KEY ("id");

INSERT INTO "usermenu" VALUES ('1', '1', '1', 'read-write');
INSERT INTO "usermenu" VALUES ('2', '1', '2', 'read-write');
INSERT INTO "usermenu" VALUES ('3', '1', '3', 'read-write');
INSERT INTO "usermenu" VALUES ('4', '1', '6', 'read-write');
INSERT INTO "usermenu" VALUES ('5', '1', '5', 'read-write');
INSERT INTO "usermenu" VALUES ('6', '1', '4', 'read-write');
INSERT INTO "usermenu" VALUES ('7', '1', '7', 'read-only');
INSERT INTO "usermenu" VALUES ('8', '1', '8', 'read-write');
INSERT INTO "usermenu" VALUES ('9', '1', '9', 'read-write');
