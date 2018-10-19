package com.fileTransfer;

import com.fileTransfer.custom.service.PersonalDAO;
import com.fileTransfer.custom.service.PersonalRest;
import com.rest.frame.exception.UsualException;
import com.rest.frame.model.Data;
import com.rest.frame.model.Field;
import com.rest.frame.model.RestResponse;
import com.rest.frame.model.Table;
import com.rest.frame.service.BaseMapper;
import com.rest.frame.service.UniversalDAO;
import com.rest.frame.util.UniversalUtil;
import org.apache.ibatis.session.SqlSession;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;
import org.junit.Test;
import sviolet.smcrypto.SM3Digest;
import sviolet.smcrypto.util.ByteUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nano on 17-7-25.
 */
public class JunitTest {

    @Test
    public void test0() throws UsualException {
//        SM2Cipher.KeyPair k = new SM2Cipher().generateKeyPair();
//        System.out.println(ByteUtils.bytesToHex(k.getPublicKey()));
//        System.out.println(new SM2Cipher().verifySignByBytes(ByteUtils.hexToBytes("31323334353637383132333435363738"), ByteUtils.hexToBytes("04" + "EA73ADED9F167F5BE13CFC5D2B47AC76B87A55504F3B242E22AC1DEB65C2E3A6DAD71DF6FBF40E1A4610D229D46B47DB081C2D1CA75D857C745421D3854A69E3"), ("test" + "1516342727384").getBytes(), ByteUtils.hexToBytes("9D53059E35345F074122A994406F90AB205CA2DB609B165577F65B47AF265BD798B753599CEF6A36FB821E9959C81E7167A72CE7139A92B3DDF10C0F008D5C37")));
//
//
//
//        System.out.println(ByteUtils.bytesToHex("test011516262927432".getBytes()));
//        System.out.println(BCrypt.hashpw("492684c1b4f08a6fe15fa85fbbbc479d6778e645255af3614aa435e638d7ae27", BCrypt.gensalt(8)));


//        InternalCache cache = InternalCache.getInstance();
//        UniversalDAO dao = UniversalDAO.getInstance();
//        Cache<String, Table> tables = cache.getTables();
//        myAssert(tables.containsKey("node"), new TableNotExistException("node"));
//        Table table = tables.get("node");
//        for (int i = 0; i < 20; i ++) {
//            SqlSession session = dao.getSession();
//            dao.getMaxIndexOfTable(table, session);
//            dao.getNumbersOfTable(table, null);
//            dao.selectList(table, 10, 1, 1, true, null);
//            session.close();
//        }

//        try {
//            HsmService hsm = new HsmService();
//            System.out.println(hsm.randomSm2KeyPair());
//        } catch (UsualException e) {
//            System.out.println(e.getMessage());
//        }

        RestResponse restResponse = new RestResponse();
        List<List<Data>> datas = new ArrayList<>();
        List<Data> ds = new ArrayList<>();
        Data d1 = new Data("id", "");
        Data d2 = new Data("nodeid", "2");
        Data d3 = new Data("comment", "test01");
        Data d4 = new Data("data", "");
        Data d5 = new Data("createtime", "");
        ds.add(d1);
        ds.add(d2);
        ds.add(d3);
        ds.add(d4);
        ds.add(d5);
        datas.add(ds);
        restResponse.setDatas(datas);
        RestResponse res = new PersonalRest().before(restResponse, "config", "Insert");
        res.getDatas().forEach(d ->
            d.forEach(data -> {
                System.out.println(data.getKey() + ":" + data.getValue());
            })
        );

//        Response res = new PersonalRest().universalSelect("config", new RestResponse());
//        System.out.println(res.toString());
//        List<List<Data>> datas = ((RestResponse) res.getEntity()).getDatas();
//        datas.forEach(ds -> {
//            ds.forEach(d -> {
//                System.out.println(d.getKey() + ":" + d.getValue());
//            });
//        });
    }

    @Test
    public void test1() {
        final URL myUrl = this.getClass().getResource("/ehcache.xml");
        XmlConfiguration xmlConfig = new XmlConfiguration(myUrl);
        CacheManager myCacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        myCacheManager.init();
        Cache<String, Table> tableCache = myCacheManager.getCache("tableCache", String.class, Table.class);

        tableCache.put("user", new Table());
        tableCache.put("menu", new Table());

        System.out.println(tableCache.containsKey("user"));
        myCacheManager.close();
    }

    @Test
    public void test2() throws UsualException {
        SqlSession session = UniversalDAO.getInstance().getSession();
        //Table table = session.selectOne("com.rest.baseMapper.selectTable", 1);
        List<Table> tables = session.selectList("com.rest.frame.service.BaseMapper.selectTables");
        System.out.println(tables.get(0).getDb_name());
    }

    @Test
    public void test3() throws UsualException {
        SqlSession session = UniversalDAO.getInstance().getSession();
        BaseMapper mapper = session.getMapper(BaseMapper.class);
        List<Table> tables = mapper.selectTables();
        List<Field> fields = mapper.selectFields();

        final URL myUrl = this.getClass().getResource("/ehcache.xml");
        XmlConfiguration xmlConfig = new XmlConfiguration(myUrl);
        final CacheManager myCacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        myCacheManager.init();
        final Cache<String, Table> tableCache = myCacheManager.getCache("tableCache", String.class, Table.class);
        // 遍历封装Table，并保存至Ehcache
        tables.forEach(t -> {
            List<Field> t_fields = new ArrayList<>();
            for (Field f : fields)
                if (f.getDb_tableid() == t.getDb_id())
                    t_fields.add(f);
            t.setFields(t_fields);
            tableCache.put(t.getDb_name(), t);
        });
        System.out.println(tableCache.containsKey("menu"));
    }

    @Test
    public void test4() throws UsualException {
        SqlSession session = UniversalDAO.getInstance().getSession();
        BaseMapper mapper = session.getMapper(BaseMapper.class);
        List<Table> tables = mapper.selectTables();
        List<Field> fields = mapper.selectFields();

        final URL myUrl = this.getClass().getResource("/ehcache.xml");
        XmlConfiguration xmlConfig = new XmlConfiguration(myUrl);
        final CacheManager myCacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        myCacheManager.init();
        final Cache<String, Table> tableCache = myCacheManager.getCache("tableCache", String.class, Table.class);
        // 遍历封装Table，并保存至Ehcache
        tables.forEach(t -> {
            List<Field> t_fields = new ArrayList<>();
            for (Field f : fields)
                if (f.getDb_tableid() == t.getDb_id())
                    t_fields.add(f);
            t.setFields(t_fields);
            tableCache.put(t.getDb_name(), t);
        });
        UniversalDAO dao = UniversalDAO.getInstance();
        long number = dao.getNumbersOfTable(tableCache.get("user"), null);
        System.out.println(number);
    }

    @Test
    public void test5() throws UsualException {
        SqlSession session = UniversalDAO.getInstance().getSession();
        BaseMapper mapper = session.getMapper(BaseMapper.class);
        List<Table> tables = mapper.selectTables();
        List<Field> fields = mapper.selectFields();

        final URL myUrl = this.getClass().getResource("/ehcache.xml");
        XmlConfiguration xmlConfig = new XmlConfiguration(myUrl);
        final CacheManager myCacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        myCacheManager.init();
        final Cache<String, Table> tableCache = myCacheManager.getCache("tableCache", String.class, Table.class);
        // 遍历封装Table，并保存至Ehcache
        tables.forEach(t -> {
            List<Field> t_fields = new ArrayList<>();
            for (Field f : fields)
                if (f.getDb_tableid() == t.getDb_id())
                    t_fields.add(f);
            t.setFields(t_fields);
            tableCache.put(t.getDb_name(), t);
        });
        UniversalDAO dao = UniversalDAO.getInstance();

        List<Field> filters = new ArrayList<>();
        Field filter = new Field();
        filter.setDb_name("id");
        filter.setDb_type("number");
        filter.setDb_min(1);
        filter.setDb_max(5);
        filters.add(filter);

        Field filter2 = new Field();
        filter2.setDb_name("username");
        filter2.setDb_type("string");
        filter2.setDb_regex("test01");
        filters.add(filter2);

        List<List<Data>> datas = dao.selectList(tableCache.get("user"), 10, 1, 1, true, filters);
        System.out.println("number:" + datas.size());
        datas.forEach(ds -> {
            System.out.println("id: " + ds.get(0).getValue());
            System.out.println("username: " + ds.get(1).getValue());
            System.out.println("------------------------");
        });
    }

//    @Test
//    public void Test6() throws IOException {
//        SqlSession session = UniversalDAO.getInstance().getSession();
//        BaseMapper mapper = session.getMapper(BaseMapper.class);
//        List<Table> tables = mapper.selectTables();
//        List<Field> fields = mapper.selectFields();
//
//        final URL myUrl = this.getClass().getResource("/ehcache.xml");
//        XmlConfiguration xmlConfig = new XmlConfiguration(myUrl);
//        final CacheManager myCacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
//        myCacheManager.init();
//        final Cache<String, Table> tableCache = myCacheManager.getCache("tableCache", String.class, Table.class);
//        // 遍历封装Table，并保存至Ehcache
//        tables.forEach(t -> {
//            List<Field> t_fields = new ArrayList<>();
//            for (Field f : fields)
//                if (f.getDb_tableid() == t.getDb_id())
//                    t_fields.add(f);
//            t.setFields(t_fields);
//            tableCache.put(t.getDb_name(), t);
//        });
//        UniversalDAO dao = UniversalDAO.getInstance();
//        Table table = tableCache.get("user");
//        List<Data> data = new ArrayList<>();
//        Data data1 = new Data<>("id", 1L);
//        Data data2 = new Data<>("username", "test03");
//        Data data3 = new Data<>("hashpass", "FFFFFFFFF");
//        data.add(data1);
//        data.add(data2);
//        data.add(data3);
//        List<Data> dataa = new ArrayList<>();
//        Data dataa1 = new Data<>("id", 1L);
//        Data dataa2 = new Data<>("username", "test08");
//        Data dataa3 = new Data<>("hashpass", "CCCCCCCC");
//        dataa.add(dataa1);
//        dataa.add(dataa2);
//        dataa.add(dataa3);
//        List<List<Data>> datas = new ArrayList<>();
//        datas.add(data);
//        datas.add(dataa);
//        dao.insertList(table, datas);
//    }

//    @Test
//    public void test7() throws IOException {
//        SqlSession session = UniversalDAO.getInstance().getSession();
//        BaseMapper mapper = session.getMapper(BaseMapper.class);
//        List<Table> tables = mapper.selectTables();
//        List<Field> fields = mapper.selectFields();
//
//        final URL myUrl = this.getClass().getResource("/ehcache.xml");
//        XmlConfiguration xmlConfig = new XmlConfiguration(myUrl);
//        final CacheManager myCacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
//        myCacheManager.init();
//        final Cache<String, Table> tableCache = myCacheManager.getCache("tableCache", String.class, Table.class);
//        // 遍历封装Table，并保存至Ehcache
//        tables.forEach(t -> {
//            List<Field> t_fields = new ArrayList<>();
//            for (Field f : fields)
//                if (f.getDb_tableid() == t.getDb_id())
//                    t_fields.add(f);
//            t.setFields(t_fields);
//            tableCache.put(t.getDb_name(), t);
//        });
//        UniversalDAO dao = UniversalDAO.getInstance();
//        Table table = tableCache.get("user");
//        List<Data> data = new ArrayList<>();
//        Data data1 = new Data<>("id", 5L);
//        Data data2 = new Data<>("username", "test05");
//        Data data3 = new Data<>("hashpass", "FFFFFFFFF");
//        data.add(data1);
//        data.add(data2);
//        data.add(data3);
//        List<Data> dataa = new ArrayList<>();
//        Data dataa1 = new Data<>("id", 6L);
//        Data dataa2 = new Data<>("username", "test06");
//        Data dataa3 = new Data<>("hashpass", "CCCCCCCC");
//        dataa.add(dataa1);
//        dataa.add(dataa2);
//        dataa.add(dataa3);
//        List<List<Data>> datas = new ArrayList<>();
//        datas.add(data);
//        datas.add(dataa);
//        dao.updateList(table, datas);
//    }
//
//    @Test
//    public void test8() throws IOException {
//        SqlSession session = UniversalDAO.getInstance().getSession();
//        BaseMapper mapper = session.getMapper(BaseMapper.class);
//        List<Table> tables = mapper.selectTables();
//        List<Field> fields = mapper.selectFields();
//
//        final URL myUrl = this.getClass().getResource("/ehcache.xml");
//        XmlConfiguration xmlConfig = new XmlConfiguration(myUrl);
//        final CacheManager myCacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
//        myCacheManager.init();
//        final Cache<String, Table> tableCache = myCacheManager.getCache("tableCache", String.class, Table.class);
//        // 遍历封装Table，并保存至Ehcache
//        tables.forEach(t -> {
//            List<Field> t_fields = new ArrayList<>();
//            for (Field f : fields)
//                if (f.getDb_tableid() == t.getDb_id())
//                    t_fields.add(f);
//            t.setFields(t_fields);
//            tableCache.put(t.getDb_name(), t);
//        });
//        UniversalDAO dao = UniversalDAO.getInstance();
//        Table table = tableCache.get("user");
//        List<Data> data = new ArrayList<>();
//        Data data1 = new Data<>("id", 7L);
//        Data data2 = new Data<>("username", "test05");
//        Data data3 = new Data<>("hashpass", "FFFFFFFFF");
//        data.add(data1);
//        data.add(data2);
//        data.add(data3);
//        List<List<Data>> datas = new ArrayList<>();
//        datas.add(data);
//        dao.deleteList(table, datas);
//    }
//
//    @Test
//    public void test9() throws IOException {
//        SqlSession session = UniversalDAO.getInstance().getSession();
//        BaseMapper mapper = session.getMapper(BaseMapper.class);
//        List<Table> tables = mapper.selectTables();
//        List<Field> fields = mapper.selectFields();
//        List<Option> options = mapper.selectOptions();
//
//        final URL myUrl = this.getClass().getResource("/ehcache.xml");
//        XmlConfiguration xmlConfig = new XmlConfiguration(myUrl);
//        final CacheManager myCacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
//        myCacheManager.init();
//        Cache<String, Table> tableCache = myCacheManager.getCache("tableCache", String.class, Table.class);
//        Cache<Long, Field> fieldCache = myCacheManager.getCache("fieldCache", Long.class, Field.class);
//        Cache<Long, Option> optionCache = myCacheManager.getCache("optionCache", Long.class, Option.class);
//
//        fields.forEach(f -> {
//            fieldCache.put(f.getDb_id(), f);
//        });
//
//        // 遍历封装Table，并保存至Ehcache
//        tables.forEach(t -> {
//            List<Field> t_fields = new ArrayList<>();
//            for (Field f : fields)
//                if (f.getDb_tableid() == t.getDb_id())
//                    t_fields.add(f);
//            t.setFields(t_fields);
//            tableCache.put(t.getDb_name(), t);
//        });
//        options.forEach(o -> {
//            Field field_index = fieldCache.get(o.getDb_index_id());
//            o.setField_index(field_index);
//            Field field_value = fieldCache.get(o.getDb_value_id());
//            o.setField_value(field_value);
//            for (int i = 0; i < tables.size(); i ++) {
//                if (tables.get(i).getDb_id() == field_index.getDb_tableid()) {
//                    o.setTable(tableCache.get(tables.get(i).getDb_name()));
//                    break;
//                }
//            }
//            optionCache.put(o.getDb_id(), o);
//        });
//
//        UniversalDAO dao = UniversalDAO.getInstance();
//        Option option = optionCache.get(3L);
//        dao.selectOption(option);
//
//    }
//
//    @Test
//    public void test10() throws IOException {
//        SqlSession session = UniversalDAO.getInstance().getSession();
//        BaseMapper mapper = session.getMapper(BaseMapper.class);
//        List<Table> tables = mapper.selectTables();
//        List<Field> fields = mapper.selectFields();
//        List<Option> options = mapper.selectOptions();
//
//        final URL myUrl = this.getClass().getResource("/ehcache.xml");
//        XmlConfiguration xmlConfig = new XmlConfiguration(myUrl);
//        final CacheManager myCacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
//        myCacheManager.init();
//        Cache<String, Table> tableCache = myCacheManager.getCache("tableCache", String.class, Table.class);
//        Cache<Long, Field> fieldCache = myCacheManager.getCache("fieldCache", Long.class, Field.class);
//        Cache<Long, Option> optionCache = myCacheManager.getCache("optionCache", Long.class, Option.class);
//
//        fields.forEach(f -> {
//            fieldCache.put(f.getDb_id(), f);
//        });
//
//        // 遍历封装Table，并保存至Ehcache
//        tables.forEach(t -> {
//            List<Field> t_fields = new ArrayList<>();
//            for (Field f : fields)
//                if (f.getDb_tableid() == t.getDb_id())
//                    t_fields.add(f);
//            t.setFields(t_fields);
//            tableCache.put(t.getDb_name(), t);
//        });
//        options.forEach(o -> {
//            Field field_index = fieldCache.get(o.getDb_index_id());
//            o.setField_index(field_index);
//            Field field_value = fieldCache.get(o.getDb_value_id());
//            o.setField_value(field_value);
//            for (int i = 0; i < tables.size(); i ++) {
//                if (tables.get(i).getDb_id() == field_index.getDb_tableid()) {
//                    o.setTable(tableCache.get(tables.get(i).getDb_name()));
//                    break;
//                }
//            }
//            optionCache.put(o.getDb_id(), o);
//        });
//
//        UniversalDAO dao = UniversalDAO.getInstance();
//        System.out.println(dao.getMaxIndexOfTable(tableCache.get("node"), session));
//    }
//
//    @Test
//    public void test11() throws Exception {
//        PersonalOption option = new PersonalOption();
//        Map map = option.universalSelect(3L);
//        System.out.println(map);
//    }

    @Test
    public void test12() throws UsualException {
        PersonalDAO dao = new PersonalDAO();
        dao.getOverview();

    }

//    @Test
//    public void test13() throws UsualException {
//        TcpService tcp = new TcpService("182.92.171.39", 3333, 0);
//        tcp.send("LG");
//    }

    @Test
    public void test14() throws UsualException {
        SM3Digest digest = new SM3Digest();
        digest.update("4E6F646530313139322E3136382E302E3130303132333435354334334542414131424444454631313731443231304530313645304635433146363430314341393638434538343539434341463832314444434238383234364546314339364630393636313545453134323143323737433146354434354643383430464131383833303135373042464244353745453942363342383336353632393733333737323131363244304230414342443235413646414531453745303138374330304435414446354238453646394143423933463136303233373635434533313534314643453044374245413237444234444641354339434336374334434531353935384446323831433933323541434543373337304530323346324441453443354545453744424441394539464342444142423342353046463139304339454630464335324141333845393742454431424532353030363531453535463843434434324445333146444536413946383039453545363044373031363230343830313139322E3136382E302E31303131323334352F7465737435353642383830443037303341424531383138433946303543303831433738463330393843413833353443433332344145443535444438333538463632373437453535444637353541304334313137363441343338304536324543443339304438424231364133373742453135384544443336343537373838343432413530363139322E3136382E302E31303231323334352F7465737430363732434531393431383145423233384130414443333444453539353735383341444231353735333845384233384641374537373243374344363132453943443445363237313245393831333633303246434441433938423639364334304643394236303633334146313742323433434332344134314539334145304242372F746573742A2E646F632C2E7478742C2E646F6378747275657472756574727565".getBytes());
        String ssid = ByteUtils.bytesToHex(digest.doFinal()).toUpperCase();
        System.out.println(ssid);
    }

    @Test
    public void test15() throws UsualException {
        String a1 = "update \"user\" set ${values} where \"id\"=1";
        String a2 = "declare\n";
        String a3 = "arg3:='EA73ADED9F167F5BE13CFC5D2B47AC76B87A55504F3B242E22AC1DEB65C2E3A6DAD71DF6FBF40E1A4610D229D46B47DB081C2D1CA75D857C745421D3854A69E3';\n";
        List<String> sets = new ArrayList<>();
        sets.add("\"id\" = 'aaaa'");
        sets.add("\"name\" = 'bbbb'");
        sets.add("\"arg3\" = arg3");
        a2 += "begin\n" + a3 + a1.replaceAll("\\$\\{values}", UniversalUtil.spellArgs(sets, ", ")) + ";\nend;";
        System.out.println(a2);

    }
}
