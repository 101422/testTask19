package ru.vvi.testtask.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.vvi.testtask.jwt.AuthService;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage(){

        return "login";
    }

}
