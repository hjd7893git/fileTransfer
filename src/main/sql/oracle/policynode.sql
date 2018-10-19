DROP TABLE "policynode";
CREATE TABLE "policynode" (
  "id" NUMBER(20) VISIBLE NOT NULL ,
  "policyid" NUMBER(20) VISIBLE NOT NULL ,
  "nodeid" NUMBER(20) VISIBLE NOT NULL ,
  "path" VARCHAR2(1024 BYTE) VISIBLE NOT NULL
)
NOLOGGING
NOCOMPRESS
NOCACHE;

ALTER TABLE "policynode" ADD CHECK ("id" IS NOT NULL);
ALTER TABLE "policynode" ADD CHECK ("policyid" IS NOT NULL);
ALTER TABLE "policynode" ADD CHECK ("nodeid" IS NOT NULL);
ALTER TABLE "policynode" ADD CHECK ("path" IS NOT NULL);

ALTER TABLE "policynode" ADD PRIMARY KEY ("id");

INSERT INTO "policynode" VALUES ('1', '1', '2', '/test');
INSERT INTO "policynode" VALUES ('2', '2', '2', '/test02');
INSERT INTO "policynode" VALUES ('3', '3', '2', '/test03');
