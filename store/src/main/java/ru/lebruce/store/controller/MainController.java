package ru.lebruce.store.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/secured")
public class MainController {
    @GetMapping("/user")
    public String userAccess(Principal principal){
        if(principal==null)
            return null;
        return principal.getName();
    }
}
