package com.in28minutes.microservices.apigateway;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder){
        Function<PredicateSpec, Buildable<Route>> routeFunction =
                predicateSpec -> predicateSpec.path("/get")
                    .filters(f -> f.addRequestHeader("MyHeader", "MyURI")
                    .addRequestParameter("Param", "MyParam"))
                    .uri("http://httpbin.org:80");
        return builder.routes()
                .route(routeFunction)
                .route(predicateSpec -> predicateSpec.path("/currency-exchange/**")
                    .uri("lb://currency-exchange"))
                .route(predicateSpec -> predicateSpec.path("/currency-conversion-feign/**")
                        .uri("lb://currency-conversion"))
                .route(predicateSpec -> predicateSpec.path("/currency-conversion/**")
                        .uri("lb://currency-conversion"))
                .route(predicateSpec -> predicateSpec.path("/currency-conversion-new/**")
                        .filters(f -> f.rewritePath("/currency-conversion-new/", "/currency-conversion-feign/"))
                        .uri("lb://currency-conversion"))
                .build();
    }
}
