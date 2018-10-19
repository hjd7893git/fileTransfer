DROP TABLE "base_option";
CREATE TABLE "base_option" (
  "base_id" NUMBER(20) VISIBLE NOT NULL ,
  "base_name" VARCHAR2(256 BYTE) VISIBLE NOT NULL ,
  "base_value" VARCHAR2(256 BYTE) VISIBLE NOT NULL ,
  "base_view" VARCHAR2(256 BYTE) VISIBLE NOT NULL
)
NOLOGGING
NOCOMPRESS
NOCACHE;

ALTER TABLE "base_option" ADD CHECK ("base_id" IS NOT NULL);
ALTER TABLE "base_option" ADD CHECK ("base_name" IS NOT NULL);
ALTER TABLE "base_option" ADD CHECK ("base_value" IS NOT NULL);
ALTER TABLE "base_option" ADD CHECK ("base_view" IS NOT NULL);

ALTER TABLE "base_option" ADD PRIMARY KEY ("base_id");

INSERT INTO "base_option" VALUES ('1', 'Authroity', 'read-write', '读/写');
INSERT INTO "base_option" VALUES ('2', 'Authroity', 'read-only', '只读');
INSERT INTO "base_option" VALUES ('3', 'Boolean', '1', '是');
INSERT INTO "base_option" VALUES ('4', 'Boolean', '0', '否');
INSERT INTO "base_option" VALUES ('5', 'Action', 'Insert', '新增');
INSERT INTO "base_option" VALUES ('6', 'Action', 'Update', '修改');
INSERT INTO "base_option" VALUES ('7', 'Action', 'Delete', '删除');
INSERT INTO "base_option" VALUES ('14', 'ConfigStatus', '1', '已启用');
INSERT INTO "base_option" VALUES ('15', 'ConfigStatus', '0', '未启用');
INSERT INTO "base_option" VALUES ('8', 'Dispose', 'undispose', '未处理');
INSERT INTO "base_option" VALUES ('9', 'Dispose', 'dispose', '已解决');
INSERT INTO "base_option" VALUES ('10', 'Dispose', 'ignore', '忽略');
INSERT INTO "base_option" VALUES ('11', 'FileStatus', 'sending', '传输中');
INSERT INTO "base_option" VALUES ('12', 'FileStatus', 'success', '传输成功');
INSERT INTO "base_option" VALUES ('13', 'FileStatus', 'failure', '传输失败');
