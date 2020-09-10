package com.formation.spring_07_sep.controllers.admin;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin") // ça nous evite d'écrire à chaque fois /admin
public class AdminIndexController {

    @GetMapping("")
    public String index() {
        return "admin/index";
    }


}
