DROP TABLE "db_table";
CREATE TABLE "db_table" (
  "db_id" NUMBER(20) VISIBLE NOT NULL ,
  "db_name" VARCHAR2(256 BYTE) VISIBLE NOT NULL ,
  "db_view" VARCHAR2(256 BYTE) VISIBLE NOT NULL ,
  "db_tip" VARCHAR2(1024 BYTE) VISIBLE NOT NULL ,
  "db_type" VARCHAR2(256 BYTE) VISIBLE NOT NULL ,
  "db_index" NUMBER(20) VISIBLE NOT NULL
)
NOLOGGING
NOCOMPRESS
NOCACHE;

ALTER TABLE "db_table" ADD CHECK ("db_id" IS NOT NULL);
ALTER TABLE "db_table" ADD CHECK ("db_name" IS NOT NULL);
ALTER TABLE "db_table" ADD CHECK ("db_view" IS NOT NULL);
ALTER TABLE "db_table" ADD CHECK ("db_tip" IS NOT NULL);
ALTER TABLE "db_table" ADD CHECK ("db_type" IS NOT NULL);
ALTER TABLE "db_table" ADD CHECK ("db_index" IS NOT NULL);

ALTER TABLE "db_table" ADD PRIMARY KEY ("db_id");

INSERT INTO "db_table" VALUES ('1', 'user', '用户', '可在此页面管理所有用户信息', 'read-write', '1');
INSERT INTO "db_table" VALUES ('2', 'menu', '菜单', '可在此页面管理系统中的所有菜单', 'read-write', '11');
INSERT INTO "db_table" VALUES ('3', 'usermenu', '用户菜单', '可在此页面管理为不同用户显示的菜单，以及相应页面的权限', 'read-write', '21');
INSERT INTO "db_table" VALUES ('4', 'node', '传输节点管理', '可在此页面管理传输节点的配置信息', 'read-write', '31');
INSERT INTO "db_table" VALUES ('5', 'policy', '传输通道管理', '可在此页面管理传输通道的配置信息', 'read-write', '41');
INSERT INTO "db_table" VALUES ('6', 'policynode', '传输目标管理', '可在此页面管理传输通道的目地节点信息', 'read-write', '51');
INSERT INTO "db_table" VALUES ('7', 'history', '操作历史', '自动记录用户的操作信息', 'read-only', '101');
INSERT INTO "db_table" VALUES ('8', 'config', '配置管理', '管理节点的配置信息', 'read-write', '61');
INSERT INTO "db_table" VALUES ('9', 'warn', '节点警告', '各传输节点异常反馈', 'read-write', '71');
