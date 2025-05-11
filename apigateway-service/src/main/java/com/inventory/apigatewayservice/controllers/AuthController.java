package com.inventory.apigatewayservice.controllers;

import com.inventory.apigatewayservice.models.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/auth")
public class AuthController {

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
}
