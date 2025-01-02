package com.example.TrendAnalyzerApplication.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class SpotifyConnectionConfig {

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    @Setter(AccessLevel.NONE)
    private String authorizationScope = "user-read-private playlist-read-private playlist-modify-public user-library-read user-read-recently-played streaming app-remote-control";

    @Value("${spotify.redirect.url}")
    private String spotifyAuthorizationRedirectURL;

    @Setter(AccessLevel.NONE)
    private String tokenUrl = "https://accounts.spotify.com/api/token";

}
