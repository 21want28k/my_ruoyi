package com.ruoyi.system.api.fallbackFactory;

import com.ruoyi.common.core.web.domain.RequestResult;
import com.ruoyi.system.api.model.LoginUser;
import com.ruoyi.system.api.service.RemoteUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteUserFallbackFactory.class);


    @Override
    public RemoteUserService create(Throwable cause) {
        log.error("远程用户服务调用失败:{}", cause.getMessage());
        return new RemoteUserService() {
            @Override
            public RequestResult<LoginUser> getUserInfo(String username, String source) {
                return RequestResult.fail("远程获取用户失败: " + cause.getMessage());
            }
        };
    }
}