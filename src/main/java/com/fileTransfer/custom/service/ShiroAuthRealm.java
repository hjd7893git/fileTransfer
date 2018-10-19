package com.fileTransfer.custom.service;

import com.rest.frame.exception.UsualException;
import com.rest.frame.model.Data;
import com.rest.frame.model.Field;
import com.rest.frame.model.Table;
import com.rest.frame.service.InternalCache;
import com.rest.frame.service.UniversalDAO;
import com.rest.frame.util.UniversalUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.ehcache.Cache;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sviolet.smcrypto.SM2Cipher;
import sviolet.smcrypto.exception.InvalidKeyDataException;
import sviolet.smcrypto.exception.InvalidSignDataException;
import sviolet.smcrypto.util.ByteUtils;

import java.util.Collections;
import java.util.List;


/**
 * Shiro认证器
 * Created by nano on 18-4-7.
 */
public class ShiroAuthRealm extends AuthorizingRealm {
    private static Logger logger = LoggerFactory.getLogger(ShiroAuthRealm.class);
    private InternalCache cache;
    private UniversalDAO dao;
    private Cache<String, Table> tables;

    public ShiroAuthRealm() throws UsualException {
        this.cache = InternalCache.getInstance();
        this.dao = UniversalDAO.getInstance();
        this.tables = cache.getTables();
        // 修改Realm支持的Token类型
        setAuthenticationTokenClass(SignToken.class);
    }

    /**
     * Shiro权限认证
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 系统没有采用 用户 -> 角色 -> 权限 的模式
        return null;
    }

    /**
     * Shiro登录认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        SignToken token = (SignToken) authenticationToken;

        // 无密码钥匙环境调试时设置成false
//        boolean validateSign = false;
        boolean validateSign = true;

        // 防SQL注入
        String username = token.getUsername().replaceAll("([';])+|(--)+"," ");
        String password = new String(token.getPassword());
        String timestamp = token.getTimestamp();
        String sign = token.getSign();
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, password, getName());
        if (logger.isDebugEnabled()) logger.debug("username:{},password:{},timestamp:{},sign:{}", username, password, timestamp, sign);
        Table userTable = tables.get("user");
        Field usernameField = new Field(0L, "username", "", "", "string", 0, 0, username, true, true, true, true, true, true, true, false, true, true, 0L, 0L, 0L);

        List<Field> searchUsernameFields = Collections.singletonList(usernameField);
        List<List<Data>> resultUsers = dao.selectList(userTable, Integer.MAX_VALUE, 1, 1, true, searchUsernameFields);
        if (logger.isDebugEnabled()) logger.debug("db result length:{}", resultUsers.size());
        if (logger.isDebugEnabled() && resultUsers.size() > 0) for (int i = 0; i < resultUsers.get(0).size(); i ++) {
            logger.debug("key:{},value:{}", resultUsers.get(0).get(i).getKey(), resultUsers.get(0).get(i).getValue());
        }

        // 为防止攻击者猜测到用户不存在，故返回模糊信息
        if (resultUsers.size() < 1)
            throw new IncorrectCredentialsException("用户名或密码不正确");
        // 签名认证
        if (validateSign) {
            String userpk = (String) UniversalUtil.gainValue(resultUsers.get(0), "userpk");
            try {
                if (!new SM2Cipher().verifySignByBytes(ByteUtils.hexToBytes("31323334353637383132333435363738"), ByteUtils.hexToBytes("04" + userpk), (username + timestamp).getBytes(), ByteUtils.hexToBytes(sign)))
                    throw new CredentialsException("智能密码钥匙认不匹配");
            } catch (InvalidSignDataException e) {
                throw new CredentialsException("错误的签名数据");
            } catch (InvalidKeyDataException e) {
                throw new CredentialsException("错误的公钥数据");
            }
        }
        // 数据库中的hashpass应通过BCrypt.hashpw(String password, BCrypt.gensalt(int log_rounds))产生
        String hashpass = (String) UniversalUtil.gainValue(resultUsers.get(0), "hashpass");
        if (logger.isDebugEnabled()) logger.debug(hashpass + " - " + BCrypt.checkpw(password, hashpass));
        if (!BCrypt.checkpw(password, hashpass))
            throw new IncorrectCredentialsException("用户名或密码不正确");

        // 登录成功则返回
        return simpleAuthenticationInfo;
    }
}
