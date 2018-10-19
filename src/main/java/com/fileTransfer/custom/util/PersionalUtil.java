package com.fileTransfer.custom.util;

import org.apache.shiro.authc.AuthenticationException;

import java.util.Random;

/**
 * Created by nano on 18-4-19.
 */
public class PersionalUtil {
    public static void myAssert(boolean expression, AuthenticationException ex) throws AuthenticationException {
        if (!expression) {
            throw ex;
        }
    }

    public static char randomChar() {
        int low = 33;
        int high = 127;
        return (char) (new Random().nextInt(high - low) + low);
    }
}
