package com.ruoyi.system.service.impl;

import com.ruoyi.system.api.domain.SysOperLog;
import com.ruoyi.system.mapper.SysOperLogMapper;
import com.ruoyi.system.service.SysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysOperLogServiceImpl implements SysOperLogService {

    @Autowired
    private SysOperLogMapper sysOperLogMapper;

    @Override
    public int insertOperlog(SysOperLog operLog) {
        return sysOperLogMapper.insertOperlog(operLog);
    }
}




