package com.example.TrendAnalyzerApplication;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class IndexController {

	SpotifyUserAuthorizationCode spotifyUserAuthorizationCode = new SpotifyUserAuthorizationCode();

	@Autowired
	AuthorizationService authorizationService;

	@GetMapping("/v1/token")
	@ResponseBody
	public String getToken(RedirectAttributes redirectAttributes) {
		JSONObject response = new JSONObject();
		if (spotifyUserAuthorizationCode.getCode() == null || spotifyUserAuthorizationCode.getCode().isEmpty()) {
			response.put("Error", "Application not authorized yet on user's behalf to access his data");
			return "error";
		}
		String token = authorizationService.getToken(spotifyUserAuthorizationCode.getCode());
		spotifyUserAuthorizationCode.setAccessToken(token);
//		spotifyUserAuthorizationCode.setRefreshToken((String) token.get("refresh_token"));
//		spotifyUserAuthorizationCode.setTokenType((String) token.get("token_type"));
		response.put("access_token", token);
		response.put("goToRecentlyPlayedLink", "http://localhost:8080/recentlyPlayed");
		redirectAttributes.addFlashAttribute("message", "Access Token Retrieved Successfully!");
		return "redirect:/recentlyPlayed";
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
