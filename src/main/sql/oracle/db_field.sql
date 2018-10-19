DROP TABLE "db_field";
CREATE TABLE "db_field" (
  "db_id" NUMBER(20) VISIBLE NOT NULL ,
  "db_name" VARCHAR2(256 BYTE) VISIBLE NOT NULL ,
  "db_view" VARCHAR2(256 BYTE) VISIBLE NOT NULL ,
  "db_tip" VARCHAR2(256 BYTE) VISIBLE DEFAULT NULL ,
  "db_type" VARCHAR2(256 BYTE) VISIBLE NOT NULL ,
  "db_min" NUMBER(20) VISIBLE DEFAULT NULL ,
  "db_max" NUMBER(20) VISIBLE DEFAULT NULL ,
  "db_regex" VARCHAR2(256 BYTE) VISIBLE DEFAULT NULL ,
  "db_cthide" NUMBER(1) VISIBLE NOT NULL ,
  "db_disable" NUMBER(1) VISIBLE NOT NULL ,
  "db_require" NUMBER(1) VISIBLE NOT NULL ,
  "db_repair" NUMBER(1) VISIBLE NOT NULL ,
  "db_search" NUMBER(1) VISIBLE NOT NULL ,
  "db_unique" NUMBER(1) VISIBLE NOT NULL ,
  "db_option" NUMBER(1) VISIBLE NOT NULL ,
  "db_relation" NUMBER(1) VISIBLE NOT NULL ,
  "db_show" NUMBER(1) VISIBLE NOT NULL ,
  "db_hide" NUMBER(1) VISIBLE NOT NULL ,
  "db_tableid" NUMBER(20) VISIBLE NOT NULL ,
  "db_optionid" NUMBER(20) VISIBLE DEFAULT NULL ,
  "db_relationid" NUMBER(20) VISIBLE DEFAULT NULL
)
NOLOGGING
NOCOMPRESS
NOCACHE;

ALTER TABLE "db_field" ADD CHECK ("db_id" IS NOT NULL);
ALTER TABLE "db_field" ADD CHECK ("db_name" IS NOT NULL);
ALTER TABLE "db_field" ADD CHECK ("db_view" IS NOT NULL);
ALTER TABLE "db_field" ADD CHECK ("db_type" IS NOT NULL);
ALTER TABLE "db_field" ADD CHECK ("db_cthide" IS NOT NULL);
ALTER TABLE "db_field" ADD CHECK ("db_disable" IS NOT NULL);
ALTER TABLE "db_field" ADD CHECK ("db_require" IS NOT NULL);
ALTER TABLE "db_field" ADD CHECK ("db_repair" IS NOT NULL);
ALTER TABLE "db_field" ADD CHECK ("db_search" IS NOT NULL);
ALTER TABLE "db_field" ADD CHECK ("db_unique" IS NOT NULL);
ALTER TABLE "db_field" ADD CHECK ("db_option" IS NOT NULL);
ALTER TABLE "db_field" ADD CHECK ("db_relation" IS NOT NULL);
ALTER TABLE "db_field" ADD CHECK ("db_show" IS NOT NULL);
ALTER TABLE "db_field" ADD CHECK ("db_hide" IS NOT NULL);
ALTER TABLE "db_field" ADD CHECK ("db_tableid" IS NOT NULL);

ALTER TABLE "db_field" ADD PRIMARY KEY ("db_id");

INSERT INTO "db_field" VALUES ('1', 'id', '序号', '不需要填写，自动递增', 'number', '1', '9007199254740991', NULL, '1', '1', '0', '0', '0', '1', '0', '0', '1', '0', '1', NULL, NULL);
INSERT INTO "db_field" VALUES ('2', 'username', '用户名', NULL, 'string', NULL, NULL, '^[a-zA-Z0-9]{6,16}$', '0', '0', '1', '0', '1', '1', '0', '0', '1', '0', '1', NULL, NULL);
INSERT INTO "db_field" VALUES ('3', 'hashpass', '密码', '用户登录时设置', 'string', NULL, NULL, '^.*$', '1', '1', '0', '1', '0', '0', '0', '0', '0', '1', '1', NULL, NULL);
INSERT INTO "db_field" VALUES ('4', 'userpk', '公钥', '对应UKEY公钥', 'clob', NULL, NULL, '^.*$', '0', '0', '1', '1', '1', '0', '0', '0', '1', '0', '1', NULL, NULL);
INSERT INTO "db_field" VALUES ('5', 'lastip', '上次登录IP', '登录成功系统自动设置', 'string', NULL, NULL, NULL, '1', '1', '0', '0', '1', '0', '0', '0', '1', '0', '1', NULL, NULL);
INSERT INTO "db_field" VALUES ('6', 'lasttime', '上次登录时间', '登录成功系统自动设置', 'string', NULL, NULL, NULL, '1', '1', '0', '0', '1', '0', '0', '0', '1', '0', '1', NULL, NULL);
INSERT INTO "db_field" VALUES ('7', 'changepwd', '登录时修改密码', NULL, 'boolean', NULL, NULL, NULL, '0', '0', '1', '1', '1', '0', '1', '0', '1', '0', '1', '7', NULL);
INSERT INTO "db_field" VALUES ('11', 'id', '序号', '不需要填写，自动递增', 'number', '1', '9007199254740991', NULL, '1', '1', '0', '0', '0', '1', '0', '0', '1', '0', '2', NULL, NULL);
INSERT INTO "db_field" VALUES ('12', 'url', '网络地址', '页面访问地址', 'string', NULL, NULL, '^[a-zA-Z0-9]+$', '0', '0', '1', '1', '1', '0', '0', '0', '1', '0', '2', NULL, NULL);
INSERT INTO "db_field" VALUES ('13', 'view', '视图名称', '仅限中文', 'string', NULL, NULL, '^[\\u4e00-\\u9fa5]+', '0', '0', '1', '1', '1', '0', '0', '0', '1', '0', '2', NULL, NULL);
INSERT INTO "db_field" VALUES ('14', 'orders', '顺序', ' ', 'number', '1', '9007199254740991', NULL, '0', '0', '1', '1', '0', '1', '0', '0', '1', '0', '2', NULL, NULL);
INSERT INTO "db_field" VALUES ('21', 'id', '序号', '不需要填写，自动递增', 'number', '1', '9007199254740991', NULL, '1', '1', '0', '0', '0', '1', '0', '0', '1', '0', '3', NULL, NULL);
INSERT INTO "db_field" VALUES ('22', 'userid', '用户', NULL, 'number', '1', '9007199254740991', NULL, '0', '0', '1', '0', '1', '0', '1', '0', '1', '0', '3', '2', NULL);
INSERT INTO "db_field" VALUES ('23', 'menuid', '菜单', NULL, 'number', '1', '9007199254740991', NULL, '0', '0', '1', '0', '1', '0', '1', '0', '1', '0', '3', '6', NULL);
INSERT INTO "db_field" VALUES ('24', 'type', '权限', NULL, 'string', NULL, NULL, '^.*$', '0', '0', '1', '1', '0', '0', '1', '0', '1', '0', '3', '1', NULL);
INSERT INTO "db_field" VALUES ('31', 'id', '序号', '不需要填写，自动递增', 'number', '1', '9007199254740991', NULL, '1', '1', '0', '0', '0', '1', '0', '0', '1', '0', '4', NULL, NULL);
INSERT INTO "db_field" VALUES ('32', 'name', '节点名称', '字母及数字', 'string', NULL, NULL, '^[a-zA-Z]{1}[a-zA-Z0-9]*$', '0', '0', '1', '1', '1', '1', '0', '0', '1', '0', '4', NULL, NULL);
INSERT INTO "db_field" VALUES ('33', 'ip', '节点网络地址', '有效的网络地址', 'string', NULL, NULL, NULL, '0', '0', '1', '1', '1', '1', '0', '0', '1', '0', '4', NULL, NULL);
INSERT INTO "db_field" VALUES ('34', 'port', '节点服务端口', '1-65535', 'number', '1', '65535', NULL, '0', '0', '1', '1', '1', '0', '0', '0', '1', '0', '4', NULL, NULL);
INSERT INTO "db_field" VALUES ('35', 'nodevk', '节点SM2私钥值', NULL, 'string', NULL, NULL, '^.*$', '1', '1', '0', '1', '0', '0', '0', '0', '0', '0', '4', NULL, NULL);
INSERT INTO "db_field" VALUES ('36', 'cert', '节点证书内容', NULL, 'clob', NULL, NULL, '^.*$', '1', '1', '0', '1', '0', '1', '0', '0', '0', '0', '4', NULL, NULL);
INSERT INTO "db_field" VALUES ('37', 'nodepk', '节点SM2公钥值', NULL, 'string', NULL, NULL, '^.*$', '1', '1', '0', '1', '1', '0', '0', '0', '1', '0', '4', NULL, NULL);
INSERT INTO "db_field" VALUES ('38', 'speed', '节点最高限速(kb/s)', '单位KB/S, 最大524288', 'number', '1', '524288', NULL, '0', '0', '1', '1', '0', '0', '0', '0', '1', '0', '4', NULL, NULL);
INSERT INTO "db_field" VALUES ('39', 'status', '是否在线', NULL, 'boolean', NULL, NULL, NULL, '1', '1', '0', '0', '1', '0', '1', '0', '1', '0', '4', '7', NULL);
INSERT INTO "db_field" VALUES ('41', 'id', '序号', '不需要填写，自动递增', 'number', '1', '9007199254740991', NULL, '1', '1', '0', '0', '0', '1', '0', '0', '1', '0', '5', NULL, NULL);
INSERT INTO "db_field" VALUES ('42', 'name', '通道名称', '字母及数字', 'string', NULL, NULL, '^[a-zA-Z]{1}[a-zA-Z0-9]*$', '0', '0', '1', '1', '1', '1', '0', '0', '1', '0', '5', NULL, NULL);
INSERT INTO "db_field" VALUES ('43', 'nodeid', '源节点', NULL, 'number', '1', '9007199254740991', NULL, '0', '0', '1', '1', '1', '0', '1', '0', '1', '0', '5', '4', NULL);
INSERT INTO "db_field" VALUES ('44', 'path', '源路径', NULL, 'string', NULL, NULL, NULL, '0', '0', '1', '1', '1', '0', '0', '0', '1', '0', '5', NULL, NULL);
INSERT INTO "db_field" VALUES ('45', 'enc', '是否加密', NULL, 'boolean', NULL, NULL, NULL, '0', '0', '1', '1', '1', '0', '1', '0', '1', '0', '5', '7', NULL);
INSERT INTO "db_field" VALUES ('46', 'compress', '是否压缩', NULL, 'boolean', NULL, NULL, NULL, '0', '0', '1', '1', '1', '0', '1', '0', '1', '0', '5', '7', NULL);
INSERT INTO "db_field" VALUES ('47', 'sign', '是否签名', NULL, 'boolean', NULL, NULL, NULL, '0', '0', '1', '1', '1', '0', '1', '0', '1', '0', '5', '7', NULL);
INSERT INTO "db_field" VALUES ('48', 'times', '传输检查时间点', '时间以“,”分隔，若即时传输则填“*”', 'string', NULL, NULL, NULL, '0', '0', '1', '1', '0', '0', '0', '0', '1', '0', '5', NULL, NULL);
INSERT INTO "db_field" VALUES ('49', 'extensions', '文件检查后缀', '后缀以“,”分隔，若不限制类型则填“*”', 'string', NULL, NULL, NULL, '0', '0', '1', '1', '0', '0', '0', '0', '1', '0', '5', NULL, NULL);
INSERT INTO "db_field" VALUES ('51', 'id', '序号', '不需要填写，自动递增', 'number', '1', '9007199254740991', NULL, '1', '1', '0', '0', '0', '1', '0', '0', '1', '0', '6', NULL, NULL);
INSERT INTO "db_field" VALUES ('52', 'policyid', '传输通道', NULL, 'number', '1', '9007199254740991', NULL, '0', '0', '1', '1', '1', '0', '1', '0', '1', '0', '6', '5', NULL);
INSERT INTO "db_field" VALUES ('53', 'nodeid', '目标节点', NULL, 'number', '1', '9007199254740991', NULL, '0', '0', '1', '1', '1', '0', '1', '0', '1', '0', '6', '4', NULL);
INSERT INTO "db_field" VALUES ('54', 'path', '目标路径', NULL, 'string', NULL, NULL, NULL, '0', '0', '1', '1', '0', '0', '0', '0', '1', '0', '6', NULL, NULL);
INSERT INTO "db_field" VALUES ('61', 'id', '序号', '不需要填写，自动递增', 'number', '1', '9007199254740991', NULL, '1', '1', '0', '0', '0', '1', '0', '0', '1', '0', '8', NULL, NULL);
INSERT INTO "db_field" VALUES ('62', 'nodeid', '节点', NULL, 'number', '1', '9007199254740991', NULL, '0', '0', '1', '0', '1', '0', '1', '0', '1', '0', '8', '4', NULL);
INSERT INTO "db_field" VALUES ('63', 'comment', '说明', '填写配置文件版本说明', 'text', NULL, NULL, NULL, '0', '0', '1', '0', '1', '0', '0', '0', '1', '0', '8', NULL, NULL);
INSERT INTO "db_field" VALUES ('64', 'data', '配置数据', NULL, 'clob', NULL, NULL, NULL, '0', '1', '0', '0', '0', '0', '0', '0', '0', '1', '8', NULL, NULL);
INSERT INTO "db_field" VALUES ('65', 'createtime', '创建时间', NULL, 'string', NULL, NULL, NULL, '1', '1', '0', '0', '1', '0', '0', '0', '1', '0', '8', NULL, NULL);
INSERT INTO "db_field" VALUES ('66', 'status', '状态', NULL, 'string', NULL, NULL, NULL, '1', '1', '0', '0', '1', '0', '1', '0', '1', '0', '8', '11', NULL);
INSERT INTO "db_field" VALUES ('71', 'id', '序号', '不需要填写，自动递增', 'number', '1', '9007199254740991', NULL, '1', '1', '0', '0', '0', '1', '0', '0', '1', '0', '9', NULL, NULL);
INSERT INTO "db_field" VALUES ('72', 'nodeid', '节点', NULL, 'number', '1', '9007199254740991', NULL, '0', '0', '1', '0', '1', '0', '1', '0', '1', '0', '9', '4', NULL);
INSERT INTO "db_field" VALUES ('73', 'userid', '处理人', NULL, 'number', '1', '9007199254740991', NULL, '0', '0', '1', '0', '1', '0', '1', '0', '1', '0', '9', '2', NULL);
INSERT INTO "db_field" VALUES ('74', 'errcode', '错误码', NULL, 'string', NULL, NULL, NULL, '0', '1', '0', '0', '1', '0', '0', '0', '1', '0', '9', NULL, NULL);
INSERT INTO "db_field" VALUES ('75', 'comment', '错误信息', NULL, 'text', NULL, NULL, NULL, '0', '1', '0', '0', '1', '0', '0', '0', '1', '0', '9', NULL, NULL);
INSERT INTO "db_field" VALUES ('76', 'timestamp', '发生时间', NULL, 'string', NULL, NULL, NULL, '0', '1', '0', '0', '1', '0', '0', '0', '1', '0', '9', NULL, NULL);
INSERT INTO "db_field" VALUES ('77', 'explain', '处理说明', NULL, 'text', NULL, NULL, NULL, '0', '0', '0', '1', '1', '0', '0', '0', '1', '0', '9', NULL, NULL);
INSERT INTO "db_field" VALUES ('78', 'dispose', '处理标记', NULL, 'string', NULL, NULL, NULL, '0', '0', '0', '1', '1', '0', '1', '0', '1', '0', '9', '9', NULL);
INSERT INTO "db_field" VALUES ('101', 'id', '序号', '不需要填写，自动递增', 'number', '1', '9007199254740991', NULL, '1', '1', '0', '0', '0', '1', '0', '0', '1', '0', '7', NULL, NULL);
INSERT INTO "db_field" VALUES ('102', 'userid', '用户', ' ', 'number', '1', '9007199254740991', NULL, '0', '1', '0', '0', '1', '0', '1', '0', '1', '0', '7', '2', NULL);
INSERT INTO "db_field" VALUES ('103', 'name', '修改项', ' ', 'string', NULL, NULL, NULL, '0', '1', '0', '0', '1', '0', '0', '0', '1', '0', '7', NULL, NULL);
INSERT INTO "db_field" VALUES ('104', 'action', '操作类型', ' ', 'string', NULL, NULL, NULL, '0', '1', '0', '0', '1', '0', '1', '0', '1', '0', '7', '8', NULL);
INSERT INTO "db_field" VALUES ('105', 'comment', '内容', ' ', 'clob', NULL, NULL, NULL, '0', '1', '0', '0', '0', '0', '0', '0', '1', '0', '7', NULL, NULL);
INSERT INTO "db_field" VALUES ('106', 'timestamp', '操作时间', ' ', 'string', NULL, NULL, NULL, '0', '1', '0', '0', '1', '0', '0', '0', '1', '0', '7', NULL, NULL);
