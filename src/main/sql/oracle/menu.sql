DROP TABLE "menu";
CREATE TABLE "menu" (
  "id" NUMBER(20) VISIBLE NOT NULL ,
  "url" VARCHAR2(256 BYTE) VISIBLE NOT NULL ,
  "view" VARCHAR2(256 BYTE) VISIBLE NOT NULL ,
  "orders" NUMBER(20) VISIBLE NOT NULL
)
NOLOGGING
NOCOMPRESS
NOCACHE;

ALTER TABLE "menu" ADD CHECK ("id" IS NOT NULL);
ALTER TABLE "menu" ADD CHECK ("url" IS NOT NULL);
ALTER TABLE "menu" ADD CHECK ("view" IS NOT NULL);
ALTER TABLE "menu" ADD CHECK ("orders" IS NOT NULL);

ALTER TABLE "menu" ADD PRIMARY KEY ("id");

INSERT INTO "menu" VALUES ('1', 'user', '用户管理', '4');
INSERT INTO "menu" VALUES ('2', 'menu', '菜单管理', '5');
INSERT INTO "menu" VALUES ('3', 'usermenu', '用户菜单', '6');
INSERT INTO "menu" VALUES ('4', 'node', '传输节点管理', '1');
INSERT INTO "menu" VALUES ('5', 'policy', '传输通道管理', '2');
INSERT INTO "menu" VALUES ('6', 'policynode', '传输目标管理', '3');
INSERT INTO "menu" VALUES ('7', 'history', '历史记录', '11');
INSERT INTO "menu" VALUES ('8', 'config', '配置管理', '7');
INSERT INTO "menu" VALUES ('9', 'warn', '节点警告', '10');
