package com.ruoyi.system.service.impl;

import com.ruoyi.system.api.domain.SysLogininfor;
import com.ruoyi.system.mapper.SysLogininforMapper;
import com.ruoyi.system.service.SysLogininforService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hhg
 * @description 针对表【sys_logininfor(系统访问记录)】的数据库操作Service实现
 * @createDate 2022-03-23 14:48:50
 */
@Service
public class SysLogininforServiceImpl implements SysLogininforService {


    @Resource
    private SysLogininforMapper sysLogininforMapper;

    @Override
    public int insertLoginInformation(SysLogininfor logininfor) {
        return sysLogininforMapper.insertAll(logininfor);
    }
}




