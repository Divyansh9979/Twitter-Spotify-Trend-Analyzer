package com.example.TrendAnalyzerApplication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.example.TrendAnalyzerApplication.RestCallUtils.checkResponseCodeExpectedString;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.*;

@Component
@Slf4j
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
