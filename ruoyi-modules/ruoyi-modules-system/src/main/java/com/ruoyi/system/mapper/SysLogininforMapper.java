package com.ruoyi.system.mapper;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.api.domain.SysLogininfor;
import org.springframework.stereotype.Repository;

/**
 * @author hhg
 * @description 针对表【sys_logininfor(系统访问记录)】的数据库操作Mapper
 * @createDate 2022-03-23 14:48:50
 * @Entity com.ruoyi.system.SysLogininfor
 */
@Repository
public interface SysLogininforMapper {

    int insertAll(SysLogininfor sysLogininfor);
}




