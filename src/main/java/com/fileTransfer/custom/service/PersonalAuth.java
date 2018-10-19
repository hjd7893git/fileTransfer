package com.fileTransfer.custom.service;

import com.fileTransfer.custom.model.AuthResponse;
import com.rest.frame.exception.UsualException;
import com.rest.frame.model.Data;
import com.rest.frame.model.Field;
import com.rest.frame.model.Table;
import com.rest.frame.service.InternalCache;
import com.rest.frame.service.UniversalDAO;
import com.rest.frame.util.UniversalUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.ehcache.Cache;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sviolet.smcrypto.SM2Cipher;
import sviolet.smcrypto.exception.InvalidKeyDataException;
import sviolet.smcrypto.exception.InvalidSignDataException;
import sviolet.smcrypto.util.ByteUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by nano on 18-4-12.
 */
@Path("/auth")
public class PersonalAuth {
    private static Logger logger = LoggerFactory.getLogger(PersonalAuth.class);

    /**
     * 报道操作
     * @return              返回内容(时间戳、随机数)
     *
     */
    @GET
    @Path("/report")
    @Produces(MediaType.APPLICATION_JSON)
    public Response report() {
        // 如果已登录则先退出登录
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        String timestamp = "{\"timestamp\": \"" + String.valueOf(System.currentTimeMillis()) + "\"}";
        return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "content-type").entity(timestamp).build();
    }


    /**
     * 登录操作
     * @param request       请求内容(用户名、hash密码、签名值)
     * @return              返回内容(令牌)
     */
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(AuthResponse request, @Context HttpServletRequest rq) {
        InternalCache cache = null;
        UniversalDAO dao = null;
        PersonalDAO pdao = null;
        try {
            cache = InternalCache.getInstance();
            dao = UniversalDAO.getInstance();
            pdao = new PersonalDAO();
        } catch (UsualException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "content-type").entity("{\"message\":\"" + e.getMessage() + "\"}").build();
        }
        Cache<String, Table> tables = cache.getTables();
        Table userTable = tables.get("user");
        Field usernameField = new Field(0L, "username", "", "", "string", 0, 0, request.getUsername().replaceAll("([';])+|(--)+"," "), true, true, true, true, true, true, true, false, true, true, 0L, 0L, 0L);

        List<Field> searchUsernameFields = Collections.singletonList(usernameField);
        List<List<Data>> resultUsers = dao.selectList(userTable, Integer.MAX_VALUE, 1, 1, true, searchUsernameFields);
        if (resultUsers.size() < 1)
            return Response.status(Response.Status.NOT_ACCEPTABLE).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "content-type").entity("{\"message\":\"用户名或密码不正确\"}").build();
        if (Long.parseLong("" + UniversalUtil.gainValue(resultUsers.get(0), "changepwd")) == 1)
            return Response.status(Response.Status.ACCEPTED).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "content-type").build();

        SignToken token = new SignToken(request.getUsername(), request.getHashpass(), request.getSign(), request.getTimestamp());
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()){
            //使用shiro来验证
            token.setRememberMe(true);
            try {
                currentUser.login(token);//验证角色
                Session session = currentUser.getSession();
                session.setAttribute("user", request.getUsername());
                session.setAttribute("uid", Long.parseLong("" + UniversalUtil.gainValue(resultUsers.get(0), "id")));
                logger.debug("用户[{}]登入", request.getUsername());

                long id = Long.parseLong("" + UniversalUtil.gainValue(resultUsers.get(0), "id"));
                String ip = "";
                if (rq.getHeader("x-forwarded-for") != null)
                    ip = rq.getHeader("x-forwarded-for").split(",")[0];
                else if (rq.getHeader("X-Real-IP") != null)
                    ip = rq.getHeader("X-Real-IP");
                else ip = rq.getRemoteAddr();
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());;
                pdao.updateLoginInfo(id, ip, time);

            } catch (AuthenticationException e) {
//                StackTraceElement[] sts = e.getStackTrace();
//                for (int i=0; i < sts.length; i ++) {
//                    logger.info(sts[i].toString());
//                }
                logger.info("catch Exception(AuthenticationException): " + e.getMessage());
                return Response.status(Response.Status.NOT_ACCEPTABLE).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "content-type").entity("{\"message\":\"" + e.getMessage() + "\"}").build();
            }
        }

        return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "content-type").build();
    }

    /**
     * 修改密码
     * @param request       请求内容(用户名、hash密码、签名值)
     * @return              返回内容(令牌)
     */
    @POST
    @Path("/changePwd")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePwd(AuthResponse request, @Context HttpServletRequest rq) {
        InternalCache cache = null;
        UniversalDAO dao = null;
        PersonalDAO pdao = null;
        try {
            cache = InternalCache.getInstance();
            dao = UniversalDAO.getInstance();
            pdao = new PersonalDAO();
        } catch (UsualException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "content-type").entity("{\"message\":\"" + e.getMessage() + "\"}").build();
        }

        Cache<String, Table> tables = cache.getTables();
        Table userTable = tables.get("user");
        Field usernameField = new Field(0L, "username", "", "", "string", 0, 0, request.getUsername().replaceAll("([';])+|(--)+"," "), true, true, true, true, true, true, true, false, true, true, 0L, 0L, 0L);

        List<Field> searchUsernameFields = Collections.singletonList(usernameField);
        List<List<Data>> resultUsers = dao.selectList(userTable, Integer.MAX_VALUE, 1, 1, true, searchUsernameFields);
        String userpk = (String) UniversalUtil.gainValue(resultUsers.get(0), "userpk");
        try {
            if (!new SM2Cipher().verifySignByBytes(ByteUtils.hexToBytes("31323334353637383132333435363738"), ByteUtils.hexToBytes("04" + userpk), (request.getUsername() + request.getTimestamp()).getBytes(), ByteUtils.hexToBytes(request.getSign())))
                return Response.status(Response.Status.NOT_ACCEPTABLE).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "content-type").entity("{\"message\":\"智能密码钥匙认不匹配\"}").build();

        } catch (InvalidKeyDataException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "content-type").entity("{\"message\":\"错误的公钥数据\"}").build();
        } catch (InvalidSignDataException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "content-type").entity("{\"message\":\"错误的签名数据\"}").build();
        }
        long id = Long.parseLong("" + UniversalUtil.gainValue(resultUsers.get(0), "id"));
        String pwd = BCrypt.hashpw(request.getHashpass(), BCrypt.gensalt(4));
        pdao.updatePassword(id, pwd);
        SignToken token = new SignToken(request.getUsername(), request.getHashpass(), request.getSign(), request.getTimestamp());
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()){
            //使用shiro来验证
            token.setRememberMe(true);
            try {
                currentUser.login(token);//验证角色
                Session session = currentUser.getSession();
                session.setAttribute("user", request.getUsername());
                session.setAttribute("uid", Long.parseLong("" + UniversalUtil.gainValue(resultUsers.get(0), "id")));
                logger.debug("用户[{}]登入", request.getUsername());

                String ip = "";
                if (rq.getHeader("x-forwarded-for") != null)
                    ip = rq.getHeader("x-forwarded-for").split(",")[0];
                else if (rq.getHeader("X-Real-IP") != null)
                    ip = rq.getHeader("X-Real-IP");
                else ip = rq.getRemoteAddr();
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());;
                pdao.updateLoginInfo(id, ip, time);
            } catch (AuthenticationException e) {
                logger.info("catch Exception(AuthenticationException): " + e.getMessage());
                return Response.status(Response.Status.NOT_ACCEPTABLE).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "content-type").entity("{\"message\":\"" + e.getMessage() + "\"}").build();
            }
        }

        return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "content-type").entity("{\"sessionId\":\"" + currentUser.getSession().getId() + "\"}").build();
    }

    /**
     * 获取用户名(已登录)
     * 重定向到json/needLogin.json（未登录）
     * @return
     */
    @GET
    @Path("/getUserInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserInfo() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            Session session = subject.getSession();
            return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "content-type").entity("{\"httpStatus\":\"200\", \"username\":\"" + session.getAttribute("user") + "\"}").build();
        } else
            return Response.status(Response.Status.FOUND).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "content-type").header("Location", "../json/needLogin.json").build();
    }

    /**
     * 登出系统
     * @return
     */
    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            Session session = subject.getSession();
            logger.debug("用户[{}]登出", session.getAttribute("user"));
            subject.logout();
        }
        return Response.status(Response.Status.FOUND).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "content-type").header("Location", "../json/needLogin.json").build();
    }


    @GET
    @Path("/getMenu")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMenu() throws UsualException {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            Session session = subject.getSession();
            String username = (String) session.getAttribute("user");
            PersonalDAO dao = new PersonalDAO();
            List<LinkedHashMap<String, Object>> menus = dao.selectMenu(username);
            StringBuffer jsonBuffer = new StringBuffer();
            jsonBuffer.append("[");
            for (int i = 0; i < menus.size(); i ++) {
                if (i != 0)
                    jsonBuffer.append(", ");
                jsonBuffer.append("{");
                Iterator<String> keys = menus.get(i).keySet().iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    jsonBuffer.append("\"" + key + "\":\"" + menus.get(i).get(key) + "\"");
                    if (keys.hasNext())
                        jsonBuffer.append(", ");
                }
                jsonBuffer.append("}");
            }
            jsonBuffer.append("]");
            return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "content-type").entity(jsonBuffer.toString()).build();
        } else
            return Response.status(Response.Status.FOUND).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "content-type").header("Location", "../json/needLogin.json").build();

    }
}
