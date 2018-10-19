package com.fileTransfer.custom.service;

import com.rest.frame.exception.UsualException;
import com.rest.frame.model.Data;
import com.rest.frame.model.Field;
import com.rest.frame.model.Option;
import com.rest.frame.model.Table;
import com.rest.frame.service.BaseMapper;
import com.rest.frame.service.InternalCache;
import com.rest.frame.service.UniversalDAO;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by nano on 18-4-16.
 */
public class PersonalDAO {
    private static Logger logger = LoggerFactory.getLogger(PersonalDAO.class);

    private UniversalDAO dao = null;

    public PersonalDAO() throws UsualException {
        dao = UniversalDAO.getInstance();
    }

    public SqlSession getSession() {
        return dao.getSession();
    }

    public long getNumbersOfTable(Table table, List<Field> fields) {
        return dao.getNumbersOfTable(table, fields);
    }

    public long getMaxIndexOfTable(Table table, SqlSession session) throws UsualException {
        return dao.getMaxIndexOfTable(table, session);
    }

    public List<List<Data>> selectList(Table table, int pageSize, int page, int orderBy, boolean squence, List<Field> fields) {
        return dao.selectList(table, pageSize, page, orderBy, squence, fields);
    }

    public int insertList(Table table, List<List<Data>> datas) throws UsualException {
        return dao.insertList(table, datas);
    }

    public int updateList(Table table, List<List<Data>> datas) {
        return dao.updateList(table, datas);
    }

    public int deleteList(Table table, List<List<Data>> datas) {
        return dao.deleteList(table, datas);
    }

    public Map selectOption(Option option) {
        return dao.selectOption(option);
    }

    public List<LinkedHashMap<String, Object>> selectMenu(String username) {
        String sql = "select a.\"url\", a.\"view\", b.\"type\" from \"menu\" a, \"usermenu\" b, \"user\" c where a.\"id\" = b.\"menuid\" and b.\"userid\" = c.\"id\" and c.\"username\" = '" + username + "' order by a.\"orders\"";
        SqlSession session = dao.getSession();
        logger.debug(sql);
        List<LinkedHashMap<String, Object>> objs = session.getMapper(BaseMapper.class).superSelect(sql);
        session.close();
        return objs;
    }

    public List<LinkedHashMap<String, Object>> selectAllNodes() {
        String sql = "select a.\"id\", a.\"ip\", a.\"port\", a.\"status\" from \"node\" a";
        SqlSession session = dao.getSession();
        logger.debug(sql);
        List<LinkedHashMap<String, Object>> objs = session.getMapper(BaseMapper.class).superSelect(sql);
        session.close();
        return objs;
    }

    public HashMap<String, Long> getOverview() {
        // 正常节点数
        String sql1 = "select count(*) as count from \"node\" a where a.\"status\" = 1";
        // 异常节点数
        String sql2 = "select count(*) as count from \"node\" a where a.\"status\" = 0";
        // 未处理的异常数
        String sql3 = "select count(*) as count from \"warn\" a where a.\"dispose\" = 'undispose'";
        // 未完成的传输任务(文件)数
        String sql4 = "select count(*) as count from (select a.* from \"file\" a where a.\"status\" != 'success')";
        // 今日新发现的传输任务(文件)数
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String sql5 = "select count(*) as count from (select a.* from \"file\" a where substr(a.\"starttime\", 1, instr(a.\"starttime\", ' ', -1)-1) = '" + today + "')";
        // 今日新增异常数
        String sql6 = "select count(*) as count from (select a.* from \"warn\" a where substr(a.\"timestamp\", 1, instr(a.\"timestamp\", ' ', -1)-1) = '" + today + "')";
        // 总节点数
        String sql7 = "select count(*) as count from \"node\" a";

        HashMap<String, Long> res = new HashMap<>();
        List<LinkedHashMap<String, Object>> objs;
        SqlSession session = dao.getSession();
        logger.debug(sql1);
        objs = session.getMapper(BaseMapper.class).superSelect(sql1);
        res.put("onlineNodes", Long.parseLong(objs.get(0).get("COUNT").toString()));
        logger.debug(sql2);
        objs = session.getMapper(BaseMapper.class).superSelect(sql2);
        res.put("offlineNodes", Long.parseLong(objs.get(0).get("COUNT").toString()));
        logger.debug(sql3);
        objs = session.getMapper(BaseMapper.class).superSelect(sql3);
        res.put("undisposeWarns", Long.parseLong(objs.get(0).get("COUNT").toString()));
        logger.debug(sql4);
        objs = session.getMapper(BaseMapper.class).superSelect(sql4);
        res.put("unfinishedFiles", Long.parseLong(objs.get(0).get("COUNT").toString()));
        logger.debug(sql5);
        objs = session.getMapper(BaseMapper.class).superSelect(sql5);
        res.put("todayFindingFiles", Long.parseLong(objs.get(0).get("COUNT").toString()));
        logger.debug(sql6);
        objs = session.getMapper(BaseMapper.class).superSelect(sql6);
        res.put("todayWarns", Long.parseLong(objs.get(0).get("COUNT").toString()));
        logger.debug(sql7);
        objs = session.getMapper(BaseMapper.class).superSelect(sql7);
        res.put("totalNodes", Long.parseLong(objs.get(0).get("COUNT").toString()));

        session.close();

        return res;
    }

    public List<LinkedHashMap<String, Object>> getOverview2() {
        // 按日期汇总传输文件
        String sql = "select substr(a.\"starttime\", 1, instr(a.\"starttime\", ' ', -1)-1) as \"date\", count(\"starttime\") as \"find\", nvl(max(\"finish\"), 0) as \"finish\" from \"file\" a left join (select substr(b.\"starttime\", 1, instr(b.\"starttime\", ' ', -1)-1) as \"date\", count(*) as \"finish\" from \"file\" b where b.\"status\" = 'success' group by substr(b.\"starttime\", 1, instr(b.\"starttime\", ' ', -1)-1)) c on c.\"date\" = substr(a.\"starttime\", 1, instr(a.\"starttime\", ' ', -1)-1) group by substr(a.\"starttime\", 1, instr(a.\"starttime\", ' ', -1)-1) order by to_date(\"date\", 'yyyy-MM-DD')";
        SqlSession session = dao.getSession();
        logger.debug(sql);
        List<LinkedHashMap<String, Object>> objs = session.getMapper(BaseMapper.class).superSelect(sql);
        session.close();
        return objs;
    }

    public boolean updatePassword(long id, String password) {
        String sql = "update \"user\" set \"hashpass\" = '" + password + "', \"changepwd\" = 0 where \"id\" = " + id;
        SqlSession session = dao.getSession();
        logger.debug(sql);
        session.getMapper(BaseMapper.class).superSelect(sql);
        session.close();
        return true;
    }

    public boolean updateLoginInfo(long id, String ip, String time) {
        String sql = "update \"user\" set \"lastip\" = '" + ip + "', \"lasttime\" = '" + time + "' where \"id\" = " + id;
        SqlSession session = dao.getSession();
        logger.debug(sql);
        session.getMapper(BaseMapper.class).superSelect(sql);
        session.close();
        return true;
    }

    public boolean insertHistory(long uid, String tableName, String action, String comment, String time) throws UsualException {
        InternalCache cache = InternalCache.getInstance();
        Table table = cache.getTable("history");
        SqlSession session = dao.getSession();
        long max = dao.getMaxIndexOfTable(table, session);
        String sql = "declare\n" +
                "arg clob;\n" +
                "begin\n" +
                "arg:='" + comment + "';\n" +
                "insert into \"history\" values(" + (max + 1) + ", " + uid + ", '" + tableName + "', '" + action + "', arg, '" + time + "');\n" +
                "end;";
        logger.debug(sql);
        session.getMapper(BaseMapper.class).superSelect(sql);
        // 更新db_count表中对应表的db_id值
        String sql1 = "update \"db_count\" set \"db_id\" = " + (max + 1) + " where \"db_name\" = '" + table.getDb_name() + "'";
        session.getMapper(BaseMapper.class).superSelect(sql1);
        session.close();
        return true;
    }

    public boolean updateNodeStatus(long id, boolean status) {
        String sql = "update \"node\" set \"status\" = " + (status ? 1 : 0) + " where \"id\" = " + id;
        SqlSession session = dao.getSession();
        logger.debug(sql);
        session.getMapper(BaseMapper.class).superSelect(sql);
        session.close();
        return true;
    }

    public boolean updateNodeConfig(long configid, long nodeid) {
        String sql1 = "update \"config\" set \"status\" = 1 where \"id\" = " + configid;
        String sql2 = "update \"config\" set \"status\" = 0 where \"nodeid\" = " + nodeid + " and \"id\" != " + configid;
        SqlSession session = dao.getSession();
        logger.debug(sql1);
        session.getMapper(BaseMapper.class).superSelect(sql1);
        logger.debug(sql2);
        session.getMapper(BaseMapper.class).superSelect(sql2);
        session.close();
        return true;
    }
}
