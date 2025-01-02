package com.example.TrendAnalyzerApplication.controller;

import com.example.TrendAnalyzerApplication.service.AuthorizationService;
import com.example.TrendAnalyzerApplication.dto.SpotifyUserAuthorizationCode;
import com.example.TrendAnalyzerApplication.service.TopArtistsService;
import com.example.TrendAnalyzerApplication.utility.TermPeriodUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Objects;

@Controller
@Slf4j
public class IndexController {

    SpotifyUserAuthorizationCode spotifyUserAuthorizationCode = new SpotifyUserAuthorizationCode();

    @Autowired
    AuthorizationService authorizationService;

    @Autowired
    TopArtistsService topArtistsService;

    @GetMapping("/v1/token")
    @ResponseBody
    public RedirectView getToken(RedirectAttributes redirectAttributes, HttpSession httpSession) throws JsonProcessingException {
        if (Objects.isNull(spotifyUserAuthorizationCode) || Objects.isNull(spotifyUserAuthorizationCode.getCode()) || spotifyUserAuthorizationCode.getCode().isEmpty()) {
            return new RedirectView("/error");
        }
        String token = authorizationService.getToken(spotifyUserAuthorizationCode.getCode());
        if (Objects.isNull(token)) {
            return new RedirectView("/error");
        }
        spotifyUserAuthorizationCode.setAccessToken(token);
        httpSession.setAttribute("accessToken", token);
        redirectAttributes.addFlashAttribute("message", "Access Token Retrieved Successfully!");
        return new RedirectView("/v1/trendingArtists");
    }

    @GetMapping("/v1/trendingArtists")
    public String topArtistsHandler(@RequestParam(value = "term", required = false) final Integer term,
                                    final HttpSession session,
                                    final Model model) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (Objects.isNull(accessToken) || accessToken.isEmpty()) {
            return "redirect:/v1/token";
        }
        try {
            model.addAttribute("artists",
                    topArtistsService.getTopArtists(accessToken, term));
            model.addAttribute("term", TermPeriodUtility.getTerm(term));
        } catch (Exception exception) {
            return "No Data";
        }
        return "top-artists";
    }

    @RequestMapping("/responseFromSpotify")
    @ResponseBody
    public String authResponse(@RequestParam(required = false) String code, @RequestParam(required = false) String state, @RequestParam(required = false) String error, Model model) {
        if (error != null) {
            return "error";
        }
        spotifyUserAuthorizationCode.setCode(code);
        spotifyUserAuthorizationCode.setUsername("user-" + Thread.currentThread().getName());
        String htmlContent =
                "<h1>Access Granted</h1>" +
                        "<p>State: " + state + "</p>" +
                        "<p>You have successfully authorized the application.</p>" +
                        "<p><a href=\"http://localhost:8080/v1/token\">Proceed to Token Exchange</a></p>";
        return htmlContent;
    }

}
