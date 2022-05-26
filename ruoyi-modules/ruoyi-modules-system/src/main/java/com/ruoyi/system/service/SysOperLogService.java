package com.ruoyi.system.service;

import com.ruoyi.system.api.domain.SysOperLog;

public interface SysOperLogService {
    /**
     * 插入一条操作日志
     * @param operLog
     * @return
     */
    public int insertOperlog(SysOperLog operLog);
}
