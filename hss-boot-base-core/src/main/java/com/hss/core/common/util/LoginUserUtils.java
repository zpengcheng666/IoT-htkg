package com.hss.core.common.util;

import com.hss.core.common.system.vo.LoginUser;

public class LoginUserUtils {

    private static final ThreadLocal<LoginUser> currUser = new ThreadLocal<>();

    public static void setUser(LoginUser user) {
        currUser.set(user);
    }

    public static LoginUser getUser() {
        return currUser.get();
    }

    public static void remove() {
        currUser.remove();
    }
}
