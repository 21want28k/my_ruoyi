package com.xx.study.filter;

import com.xx.study.constants.FilterOrderedConstant;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
public class MyGatewayFilter2 extends AbstractGatewayFilterFactory<MyGatewayFilter2.Config> implements Ordered {

    public static final String SWITCH_FLAG = "switchFlag";
    public static final String NAME = "name";

    public MyGatewayFilter2() {
        super(Config.class);
    }

    @Override
    public String name() {
        return "MyGatewayFilter2";
    }

    /**
     * 作用就是将配置filter之后的参数和config相对应的进行绑定起来，第一个是SWITCH_FLAG，开关标志位，第二个是名字（这个是随便加的）
     * @return
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(SWITCH_FLAG, NAME);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

                if (config.isSwitchFlag()) {
                    System.out.println("MyGatewayFilter2 works 配置中的第一个参数switchFlag是" + config.isSwitchFlag());
                    System.out.println("MyGatewayFilter2 works 配置中的第二个参数name是" + config.getName());
                }
                System.out.println("MyGatewayFilter2 works 过滤器前置操作, order=" + getOrder());
                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    System.out.println("过滤器后置操作");
                }));
            }
        };
    }

    @Override
    public int getOrder() {
        return FilterOrderedConstant.MY_GATEWAY_FILTER2_ORDER;
    }

    static class Config {
        private boolean switchFlag;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSwitchFlag() {
            return switchFlag;
        }

        public void setSwitchFlag(boolean switchFlag) {
            this.switchFlag = switchFlag;
        }
    }
}
