package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class IndexController {

//    @GetMapping("/")
//    public String indexPage(Model model, @AuthenticationPrincipal OidcUser user) {
//        if (user != null) {
//            log.info("User: {}", user);
//        }
//        return "index";
//    }
}
