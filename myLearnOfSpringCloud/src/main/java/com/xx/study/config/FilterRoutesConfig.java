package com.xx.study.config;

import com.xx.study.filter.MyGatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

public class FilterRoutesConfig {

    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/mylearn123/**")
                        .filters(f -> f.stripPrefix(1)
                                .filter(new MyGatewayFilter()))
                        .uri("lb://mylearn")
                )
                .build();
    }
}