package com.ruoyi.gateway.filter;

import com.ruoyi.common.core.constant.Constants;
import com.ruoyi.common.core.constant.HttpStatus;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.constant.TokenConstants;
import com.ruoyi.common.core.utils.JwtUtils;
import com.ruoyi.common.core.utils.ServletUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.redis.service.RedisService;
import com.ruoyi.gateway.config.properties.IgnoreWhiteProperties;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Consumer;

/**
 * 网关鉴权，首先要在全局上进行
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {


    @Autowired
    private IgnoreWhiteProperties ignoreWhiteProperties;

    @Autowired
    private RedisService redisService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<String> whiteList = ignoreWhiteProperties.getWhites();
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if (StringUtils.matches(path, whiteList)) {
            return chain.filter(exchange);
        }
        String token = this.getTokenFromRequest(request);
        Claims claims = JwtUtils.parseToken(token);
        if (claims.isEmpty()) {
            return unauthorizedResponse("令牌已过期或验证不正确！", exchange);
        }
        String redisTokenKey = JwtUtils.getUserKey(claims);

        // redis中的key是UUID:loginUser对象的组合, 是否登录用redis去维护一个key
        Boolean hasKey = redisService.hasKey(redisTokenKey);
        if (!hasKey) {
            return unauthorizedResponse("登录状态已过期", exchange);
        }

        String userId = JwtUtils.getUserId(claims);
        String userName = JwtUtils.getUserName(claims);

        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userName)) {
            return unauthorizedResponse("令牌验证失败", exchange);
        }

        ServerHttpRequest.Builder mutate = request.mutate();
        addHeader(mutate, SecurityConstants.DETAILS_USER_ID, userId);
        addHeader(mutate, SecurityConstants.DETAILS_USERNAME, userName);
        addHeader(mutate, SecurityConstants.USER_KEY, redisTokenKey);

        removeHeader(mutate, SecurityConstants.FROM_SOURCE);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    public Mono<Void> unauthorizedResponse(String message, ServerWebExchange exchange) {
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), org.springframework.http.HttpStatus.UNAUTHORIZED, message, HttpStatus.UNAUTHORIZED);
    }


    public void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
        String encodeValueStr = ServletUtils.urlEncode(valueStr);
        mutate.header(name, encodeValueStr);
    }

    public void removeHeader(ServerHttpRequest.Builder mutate, String name) {
        mutate.headers(new Consumer<HttpHeaders>() {
            @Override
            public void accept(HttpHeaders httpHeaders) {
                httpHeaders.remove(name);
            }
        });
    }

    /**
     * 为了保证是在所有的过滤器之前生效
     *
     * @return
     */
    @Override
    public int getOrder() {
        return -200;
    }

    public String getTokenFromRequest(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(TokenConstants.AUTHENTICATION);
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, StringUtils.EMPTY);
        }
        return token;
    }
}
