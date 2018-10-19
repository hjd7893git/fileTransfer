package com.fileTransfer.custom.service;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

/**
 * Shiro带Sm2签名的用户登录令牌
 * Created by nano on 18-4-9.
 */
public class SignToken implements AuthenticationToken, RememberMeAuthenticationToken {

    /*--------------------------------------------
    |             C O N S T A N T S             |
    ============================================*/

    /*--------------------------------------------
    |    I N S T A N C E   V A R I A B L E S    |
    ============================================*/
    /**
     * The username
     */
    private String username;

    /**
     * The password, in char[] format
     */
    private char[] password;

    private String sign;
    private String timestamp;


    /**
     * Whether or not 'rememberMe' should be enabled for the corresponding login attempt;
     * default is <code>false</code>
     */
    private boolean rememberMe = false;

    public SignToken() {
    }

    SignToken(final String username, final String password, final String sign, final String timestamp) {
        this(username, password != null ? password.toCharArray() : null, sign, timestamp, false);
    }

    private SignToken(final String username, final char[] password, final String sign, final String timestamp,
                                 final boolean rememberMe) {

        this.username = username;
        this.password = password;
        this.sign = sign;
        this.timestamp = timestamp;
        this.rememberMe = rememberMe;
    }

    public SignToken(final String username, final String password, final String sign, final String timestamp,
                                 final boolean rememberMe) {
        this(username, password != null ? password.toCharArray() : null, sign, timestamp, rememberMe);
    }

    /*--------------------------------------------
    |  A C C E S S O R S / M O D I F I E R S    |
    ============================================*/

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String Sign) {
        this.sign = sign;
    }

    public Object getPrincipal() {
        return getUsername();
    }

    public Object getCredentials() {
        return getPassword();
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /*--------------------------------------------
    |               M E T H O D S               |
    ============================================*/

    /**
     * Clears out (nulls) the username, password, rememberMe, and inetAddress.  The password bytes are explicitly set to
     * <tt>0x00</tt> before nulling to eliminate the possibility of memory access at a later time.
     */
    public void clear() {
        this.username = null;
        this.timestamp = null;
        this.sign = null;
        this.rememberMe = false;

        if (this.password != null) {
            for (int i = 0; i < password.length; i++) {
                this.password[i] = 0x00;
            }
            this.password = null;
        }
    }

    /**
     * Returns the String representation.  It does not include the password in the resulting
     * string for security reasons to prevent accidentially printing out a password
     * that might be widely viewable).
     *
     * @return the String representation of the <tt>UsernamePasswordToken</tt>, omitting
     *         the password.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(" - ");
        sb.append(username);
        sb.append(" - ");
        sb.append(sign);
        sb.append(", rememberMe=").append(rememberMe);
        if (timestamp != null) {
            sb.append(" (").append(timestamp).append(")");
        }
        return sb.toString();
    }
}
