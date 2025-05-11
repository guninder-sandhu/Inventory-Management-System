package com.inventory.apigatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity httpSecurity, ReactiveClientRegistrationRepository clientRegistrationRepository) {

        ServerOAuth2AuthorizationRequestResolver resolver = new CustomAuthorizationRequestResolver(clientRegistrationRepository);

        return httpSecurity
                .authorizeExchange(exchange -> {
                    exchange.pathMatchers("/login", "/oauth2/**").permitAll();
                    exchange.anyExchange().authenticated();
                })
                .oauth2Login(oauth2Login -> {
                    oauth2Login.authorizationRequestResolver(resolver);
                })
                .oauth2Client(Customizer.withDefaults())
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> {
                    oAuth2ResourceServerSpec.jwt(jwtSpec -> {
                        jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor());
                    });
                }).build();
    }

    // Define a ReactiveOAuth2UserService for OIDC
    @Bean
    public ReactiveOAuth2UserService<OidcUserRequest, OidcUser> oidcReactiveOAuth2UserService() {
        final OidcReactiveOAuth2UserService delegate = new OidcReactiveOAuth2UserService();

        return (userRequest) -> {
            // Decode the access token to get its claims
            String jwkSetUri = userRequest.getClientRegistration().getProviderDetails().getJwkSetUri();
            JwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
            String accessToken = userRequest.getAccessToken().getTokenValue();
            Mono<Jwt> jwtMono = Mono.just(jwtDecoder.decode(accessToken));

            return jwtMono.flatMap(jwt -> {
                // Delegate to the default OidcReactiveOAuth2UserService to load the user
                return delegate.loadUser(userRequest).map(oidcUser -> {
                    // Extract authorities from the access token claims
                    List<GrantedAuthority> authorities = new ArrayList<>(oidcUser.getAuthorities());
                    authorities.addAll(extractAuthoritiesFromClaims(jwt.getClaims()));

                    // Create a new OidcUser with updated authorities
                    return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
                });
            });
        };
    }

    private ReactiveJwtAuthenticationConverterAdapter grantedAuthoritiesExtractor() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("ROLE_");
        converter.setAuthoritiesClaimName("https://ims-api.com/roles");

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<GrantedAuthority> authorities = new ArrayList<>(converter.convert(jwt));
            List<String> permissions = jwt.getClaimAsStringList("permissions");
            if (permissions != null) {
                authorities.addAll(permissions.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList());
            }
            return authorities;
        });

        return new ReactiveJwtAuthenticationConverterAdapter(jwtConverter);
    }

    private List<GrantedAuthority> extractAuthoritiesFromClaims(Map<String, Object> claims) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Extract roles using getClaimAsStringList
        List<String> roles = claims.get("https://ims-api.com/roles") instanceof List
                ? ((List<?>) claims.get("https://ims-api.com/roles")).stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .toList()
                : new ArrayList<>();
        if (!roles.isEmpty()) {
            authorities.addAll(roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .toList());
        }

        // Extract permissions using getClaimAsStringList
        List<String> permissions = claims.get("permissions") instanceof List
                ? ((List<?>) claims.get("permissions")).stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .toList()
                : new ArrayList<>();
        if (!permissions.isEmpty()) {
            authorities.addAll(permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList());
        }

        return authorities;
    }

    static class CustomAuthorizationRequestResolver implements ServerOAuth2AuthorizationRequestResolver {
        private final DefaultServerOAuth2AuthorizationRequestResolver delegate;

        CustomAuthorizationRequestResolver(ReactiveClientRegistrationRepository clientRegistrationRepository) {
            this.delegate = new DefaultServerOAuth2AuthorizationRequestResolver(clientRegistrationRepository);
        }

        @Override
        public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange) {
            return delegate.resolve(exchange).map(this::customize);
        }

        @Override
        public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange, String clientRegistrationId) {
            return delegate.resolve(exchange, clientRegistrationId).map(this::customize);
        }

        private OAuth2AuthorizationRequest customize(OAuth2AuthorizationRequest request) {
            Map<String, Object> additionalParameters = new HashMap<>(request.getAdditionalParameters());
            additionalParameters.put("audience", "https://ims-api");
            return OAuth2AuthorizationRequest.from(request)
                    .additionalParameters(additionalParameters)
                    .build();
        }
    }
}