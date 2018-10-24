package com.rrsol.geogov.security.web;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Secured("ROLE_ADMIN")
@RequestMapping("/sigech")
public class IndexController {
	
	
    @GetMapping("/")
    public String estatalIndex() {
        return "estatal/index";
    }
}
