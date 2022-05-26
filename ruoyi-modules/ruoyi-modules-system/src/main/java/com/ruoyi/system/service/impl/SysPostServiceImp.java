package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.mapper.SysPostMapper;
import com.ruoyi.system.service.SysPostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysPostServiceImp implements SysPostService {
    @Autowired
    private SysPostMapper sysPostMapper;

    /**
     * 通过userId获得所在岗位的post_name字符串，通过，进行拼接
     *
     * @param userId
     * @return
     */
    @Override
    public String selectUserPostGroup(Long userId) {
        List<String> posts = sysPostMapper.selectPostsByUserId(userId);
        if (CollectionUtils.isEmpty(posts)) {
            return StringUtils.EMPTY;
        } else {
            return String.join(",", posts);
        }
    }
}
