package com.ruoyi.system.api.service;


import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.web.domain.RequestResult;
import com.ruoyi.system.api.fallbackFactory.RemoteUserFallbackFactory;
import com.ruoyi.system.api.model.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.SYSTEM_MODULE_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {

    @GetMapping("/user/info/{username}")
    public RequestResult<LoginUser> getUserInfo(@PathVariable("username") String username, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}