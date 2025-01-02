package com.example.TrendAnalyzerApplication.config;

import com.example.TrendAnalyzerApplication.dto.SpotifyUserAuthorizationCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpotifyUserAuthorizationCodeConfig {

    @Bean
    public SpotifyUserAuthorizationCode spotifyUserAuthorizationCode() {
        return new SpotifyUserAuthorizationCode();
    }

}
