package com.rrsol.geogov.security.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@Value("${SUAC.url}")
	private String SUAC_url;
    @GetMapping("/")
    public String root(Model model) {
    	model.addAttribute("SUAC_url",SUAC_url);
        return "index";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/error/access-denied";
    }

}
