package com.ruoyi.system.service;

import com.ruoyi.system.api.domain.SysLogininfor;

/**
* @author hhg
* @description 针对表【sys_logininfor(系统访问记录)】的数据库操作Service
* @createDate 2022-03-23 14:48:50
*/
public interface SysLogininforService {


    public int insertLoginInformation(SysLogininfor logininfor);
}
