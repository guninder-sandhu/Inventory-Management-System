package com.inventory.apigatewayservice.controllers;

import com.inventory.apigatewayservice.models.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/auth")
public class AuthController {


//    @Value("${'spring.cloud.security.oauth2.client.registration.ims-client.client-id'}")
//    private String clientId;
//    @Value("${security.oauth2.client.registration.ims-client.client-secret}")
//    private String clientSecret;
//    @Value("${security.oauth2.client.registration.ims-client.redirect-uri}")
//    private String redirectUri;
//    @Value("${frontend.redirect-uri}")
//    private String frontendRedirect;
//    @Value("${security.oauth2.client.provider.auth0.issuer-uri}")
//    private String issuerUri;

    private Logger logger = LoggerFactory.getLogger(AuthController.class.getName());

    @GetMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RegisteredOAuth2AuthorizedClient("ims-client") OAuth2AuthorizedClient client,
            @AuthenticationPrincipal OidcUser user,
            Model model
    ) {
        logger.info("User email id is {}", user.getEmail());
        user.getClaims().forEach((key, value) -> logger.info("claim {} -> {}", key, value));


        AuthResponse authResponse = new AuthResponse();
        authResponse.setUserId(user.getEmail());
        authResponse.setAccessToken(client.getAccessToken().getTokenValue());
        authResponse.setRefreshToken(client.getRefreshToken().getTokenValue());
        authResponse.setExpireAt(client.getAccessToken().getExpiresAt().getEpochSecond());
        List<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        authResponse.setAuthorities(authorities);
        // Extract roles (authorities starting with ROLE_)
        List<String> roles = authorities.stream()
                .filter(auth -> auth.startsWith("ROLE_"))
                .map(auth -> auth.substring(5)) // Remove ROLE_ prefix
                .toList();
        authResponse.setRoles(roles);

        // Extract permissions (authorities not starting with ROLE_)
        List<String> permissions = authorities.stream()
                .filter(auth -> !auth.startsWith("ROLE_"))
                .toList();
        authResponse.setPermissions(permissions);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/callback")
    public Mono<Map> callback(@RequestBody Map<String, String> payload) {
        var authAccessCode = payload.get("code");
        var authState = payload.get("state");
        logger.info("authAccessCode is {} and state is {}", authAccessCode, authState);
        Map<String, String> payloadauth = Map.of(
                "grant_type", "authorization_code",
                "client_id", "RGBhQ6wHQjRxbvc69KFHDEJ1UKxDxWTW",
                "client_secret", "Pg96wWFA17FCpP1CGGDS-u5JGzmO7kYQxSjdakAKMiPY86af9YgE7Ju6FeCmcnWL",
                "code", authAccessCode,
                "redirect_uri", "http://localhost:5173/callback"
        );

        // Exchange code for token using WebClient
        return WebClient.create().post()
                .uri("https://dev-tifsgpj0de6wps8x.au.auth0.com/oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payloadauth)
                .retrieve()
                .bodyToMono(Map.class);


    }

    @GetMapping("/validate")
    public ResponseEntity<Void> validate(@AuthenticationPrincipal Jwt jwt) {
        logger.info("reached");


        return ResponseEntity.ok().build();

    }


}
