package GatewayService.GatewayService.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class GatewayServiceConfig {

    @Bean
    public CorsWebFilter corsWebFilter(){

        final CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Collections.singletonList("*"));
        corsConfig.setMaxAge(3600L);
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST"));
        corsConfig.addAllowedHeader("*");
        corsConfig.addExposedHeader("Authorization");

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/v1/**", corsConfig);

        return new CorsWebFilter((CorsConfigurationSource) source);

    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder,AuthenticationPreFilter authenticationPreFilter,
                                      AuthProperties authProperties,BalanceProperties balanceProperties,TransactionProperties transactionProperties,
                                      AccountProperties accountProperties,UserProperties userProperties) {
        return builder.routes()
                .route(routeToUser -> routeToUser.path(userProperties.getPath())
                        .filters(f ->
                                f.filter(authenticationPreFilter.apply(
                                        new AuthenticationPreFilter.Config())))
                        .uri(userProperties.getUri()))
                .route(routeToBalance -> routeToBalance.path(balanceProperties.getPath())
                        .filters(f ->
                                f.filter(authenticationPreFilter.apply(
                                        new AuthenticationPreFilter.Config())))
                        .uri(balanceProperties.getUri()))
                .route(routeToAccount -> routeToAccount.path(accountProperties.getPath())
                        .filters(f ->
                                f.filter(authenticationPreFilter.apply(
                                        new AuthenticationPreFilter.Config())))
                        .uri(accountProperties.getUri()))
                .route(routeToAccount -> routeToAccount.path(transactionProperties.getPath())
                        .filters(f ->
                                f.filter(authenticationPreFilter.apply(
                                        new AuthenticationPreFilter.Config())))
                        .uri(transactionProperties.getUri()))
                .route(routeToAccount -> routeToAccount.path(authProperties.getPath())
                        .uri(authProperties.getUri()))
                .build();
    }


}
