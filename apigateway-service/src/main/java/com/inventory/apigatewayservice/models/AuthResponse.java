package com.inventory.apigatewayservice.models;

import lombok.*;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthResponse {

    private String userId;
    private String accessToken;
    private String refreshToken;
    private long expireAt;
    private Collection<String> authorities;

    private List<String> roles;       // Add this
    private List<String> permissions; // Add this
}
