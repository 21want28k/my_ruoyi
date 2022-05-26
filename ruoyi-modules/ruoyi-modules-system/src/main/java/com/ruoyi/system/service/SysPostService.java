package com.ruoyi.system.service;

import java.util.List;

public interface SysPostService {

    /**
     * 通过userId获得所在岗位的post_name字符串，通过，进行拼接
     * @param userId
     * @return
     */
    String selectUserPostGroup(Long userId);
}
