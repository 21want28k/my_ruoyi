package com.xx.study.filter;

import com.xx.study.config.FilterRoutesConfig;
import com.xx.study.constants.FilterOrderedConstant;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class MyGatewayFilter implements GatewayFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("MyGatewayFilter works, order=" + this.getOrder());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return FilterOrderedConstant.MY_GATEWAY_FILTER_ORDER;
    }
}
