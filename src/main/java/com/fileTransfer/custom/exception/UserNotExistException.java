package com.fileTransfer.custom.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Created by nano on 18-4-19.
 */
public class UserNotExistException extends AuthenticationException {
    public UserNotExistException(String userName) {
        super("用户[ " + userName + " ]不存在");
    }
}
