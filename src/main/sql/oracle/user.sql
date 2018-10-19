DROP TABLE "user";
CREATE TABLE "user" (
  "id" NUMBER(20) VISIBLE NOT NULL ,
  "username" VARCHAR2(256 BYTE) VISIBLE NOT NULL ,
  "hashpass" VARCHAR2(256 BYTE) VISIBLE NOT NULL ,
  "userpk" VARCHAR2(256 BYTE) VISIBLE NOT NULL ,
  "lastip" VARCHAR2(256 BYTE) VISIBLE ,
  "lasttime" VARCHAR2(256 BYTE) VISIBLE ,
  "changepwd" NUMBER(1) VISIBLE NOT NULL
)
NOLOGGING
NOCOMPRESS
NOCACHE;

ALTER TABLE "user" ADD CHECK ("id" IS NOT NULL);
ALTER TABLE "user" ADD CHECK ("username" IS NOT NULL);
ALTER TABLE "user" ADD CHECK ("hashpass" IS NOT NULL);
ALTER TABLE "user" ADD CHECK ("userpk" IS NOT NULL);
ALTER TABLE "user" ADD CHECK ("changepwd" IS NOT NULL);

ALTER TABLE "user" ADD PRIMARY KEY ("id");

declare
  username varchar2(256 BYTE) := 'test01';
  userpk varchar2(256 BYTE) := 'EA73ADED9F167F5BE13CFC5D2B47AC76B87A55504F3B242E22AC1DEB65C2E3A6DAD71DF6FBF40E1A4610D229D46B47DB081C2D1CA75D857C745421D3854A69E3';
  begin
    INSERT INTO "user" VALUES ('1', username, ' ', userpk, ' ', ' ', '1');
  end;
