package com.ruoyi.system.mapper;

import com.ruoyi.system.api.domain.SysOperLog;
import org.springframework.stereotype.Repository;

@Repository
public interface SysOperLogMapper {
    /**
     * 插入一条操作日志
     * @param operLog
     * @return
     */
    public int insertOperlog(SysOperLog operLog);
}




