package com.ruoyi.common.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtils {

    /**
     * 验证邮箱格式是否正确
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }
}
