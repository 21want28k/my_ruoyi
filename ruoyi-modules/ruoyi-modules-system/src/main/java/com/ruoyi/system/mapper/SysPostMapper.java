package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysPostMapper {

    List<String> selectPostsByUserId(@Param("userId") Long userId);
}




