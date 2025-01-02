package com.example.TrendAnalyzerApplication.service;

import com.example.TrendAnalyzerApplication.dto.AccessTokenDto;
import com.example.TrendAnalyzerApplication.config.SpotifyConnectionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class AuthorizationService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    SpotifyConnectionConfig spotifyConnectionConfig;

    public String getToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString(
                (spotifyConnectionConfig.getClientId() + ":" + spotifyConnectionConfig.getClientSecret()).getBytes()
        ));

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", spotifyConnectionConfig.getClientId());
        map.add("grant_type", "authorization_code");
        map.add("code", code);
        map.add("redirect_uri", spotifyConnectionConfig.getSpotifyAuthorizationRedirectURL());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<AccessTokenDto> response = restTemplate.postForEntity(spotifyConnectionConfig.getTokenUrl(), request, AccessTokenDto.class);
        return response.getBody().getAccess_token();
    }
}
