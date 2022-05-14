package com.ruoyi.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.constant.Constants;
import com.ruoyi.common.core.utils.ServletUtils;
import com.ruoyi.gateway.config.properties.CaptchaProperties;
import com.ruoyi.gateway.service.ValidateCaptchaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class ValidateCodeFilter extends AbstractGatewayFilterFactory<Object> {

    private final static String[] VALIDATE_URL = new String[]{"/auth/login", "/auth/register"};

    @Autowired
    private CaptchaProperties captchaProperties;

    @Autowired
    private ValidateCaptchaService validateCaptchaService;

    @Override
    public GatewayFilter apply(Object config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

                ServerHttpRequest request = exchange.getRequest();
                String uriPath = request.getURI().getPath();
                // 验证白名单，不在名单上的不验证
                if (!StringUtils.containsAnyIgnoreCase(uriPath, VALIDATE_URL) || !captchaProperties.getEnabled()) {
                    return chain.filter(exchange);
                }

                try {
                    String str = resolveBodyFromRequest(request);
                    JSONObject obj = JSONObject.parseObject(str);
                    validateCaptchaService.checkCaptcha(obj.getString(Constants.CODE), obj.getString(Constants.UUID));
                } catch (Exception e) {
                    return ServletUtils.webFluxResponseWriter(exchange.getResponse(), e.getMessage());
                }
                return chain.filter(exchange);
            }
        };
    }

    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        // 获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        return bodyRef.get();
    }
}
