package com.xx.study.filter;

import com.xx.study.constants.FilterOrderedConstant;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class MyGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("MyGlobalFilter(全局过滤器) works, order=" + this.getOrder());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return FilterOrderedConstant.MY_GLOBAL_FILTER2_ORDER;
    }
}
