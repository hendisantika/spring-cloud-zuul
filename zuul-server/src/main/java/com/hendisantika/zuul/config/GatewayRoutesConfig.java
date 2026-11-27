package com.hendisantika.zuul.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.stripPrefix;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RequestPredicates.GET;
import static org.springframework.web.servlet.function.RequestPredicates.path;

/**
 * Created by IntelliJ IDEA.
 * Project : zuul-server
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Link : s.id/hendisantika
 * Date: 27/11/26
 * Time: 07.48
 * <p>
 * Routes formerly served by Netflix Zuul, now served by Spring Cloud Gateway Server WebMVC.
 * Every route lives under the {@code /api} prefix, which is stripped together with the route name
 * before the request is forwarded (same behaviour as {@code zuul.prefix} + {@code stripPrefix}).
 */
@Configuration(proxyBeanMethods = false)
public class GatewayRoutesConfig {

    private static final String CUSTOMER_SERVICE = "customer-service";

    /**
     * Service routed through Eureka using its service id (like Zuul auto registration).
     */
    @Bean
    public RouterFunction<ServerResponse> customerServiceRoute() {
        return route("customer-service")
                .route(path("/api/customer-service/**"), http())
                .filter(lb(CUSTOMER_SERVICE))
                .before(stripPrefix(2))
                .build();
    }

    /**
     * Service routed by explicit address.
     */
    @Bean
    public RouterFunction<ServerResponse> customerByAddressRoute(
            @Value("${gateway.routes.customer-by-address.url:http://localhost:9098}") String customerUrl) {
        return route("customer-by-address")
                .route(path("/api/customer-by-address/**"), http())
                .before(uri(customerUrl))
                .before(stripPrefix(2))
                .build();
    }

    /**
     * Service routed by Eureka service id.
     */
    @Bean
    public RouterFunction<ServerResponse> customerByServiceRoute() {
        return route("customer-by-service")
                .route(path("/api/customer-by-service/**"), http())
                .filter(lb(CUSTOMER_SERVICE))
                .before(stripPrefix(2))
                .build();
    }

    /**
     * Dynamic routing to an external host.
     */
    @Bean
    public RouterFunction<ServerResponse> redirectRoute(
            @Value("${gateway.routes.redirect.url:https://example.com}") String redirectUrl) {
        return route("redirect")
                .route(path("/api/redirect/**"), http())
                .before(uri(redirectUrl))
                .before(stripPrefix(2))
                .build();
    }

    /**
     * Static response served directly by the gateway (text, or an SVG image for {@code *.svg}).
     */
    @Bean
    public RouterFunction<ServerResponse> staticRoute() {
        Resource svg = new ClassPathResource("static/example.svg");
        return route("static")
                .route(GET("/api/static/**").and(request -> request.path().endsWith(".svg")),
                        request -> ServerResponse.ok()
                                .contentType(MediaType.valueOf("image/svg+xml"))
                                .body(readSvg(svg)))
                .GET("/api/static", request -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body("Static content"))
                .GET("/api/static/**", request -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body("Static content"))
                .build();
    }

    private static String readSvg(Resource svg) throws IOException {
        return svg.getContentAsString(StandardCharsets.UTF_8);
    }
}
