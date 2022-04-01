package com.ruoyi.system.api.fallbackFactory;

import com.ruoyi.system.api.service.RemoteLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

public class RemoteLogFallbackFactory implements FallbackFactory<RemoteLogService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteLogFallbackFactory.class);

    @Override
    public RemoteLogService create(Throwable cause) {

        log.info("日志服务调用失败:{}", cause.getMessage());
        return null;
    }
}
