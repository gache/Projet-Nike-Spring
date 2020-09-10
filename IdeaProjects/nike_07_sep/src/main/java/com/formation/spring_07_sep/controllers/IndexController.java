package com.formation.spring_07_sep.controllers;

import com.formation.spring_07_sep.emums.RoleEnum;
import com.formation.spring_07_sep.models.User;
import com.formation.spring_07_sep.services.ArticleService;
import com.formation.spring_07_sep.services.UserService;
import com.formation.spring_07_sep.utils.PasswordEncoderJava;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;


@Controller
public class IndexController {

    private ArticleService articleService;
    private UserService userService;

    // constructor
    @Autowired
    public IndexController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }


    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/video")
    public String video(Model model) {
        model.addAttribute("fragment", "video");
        return "index";
    }

    @GetMapping("/articles")
    public String Articles(Model model) {
        //j'envoie ma liste a la vu
        model.addAttribute("articles", articleService.findAll());
        model.addAttribute("fragment", "article");
        return "index";
    }

    @GetMapping("/inscription")
    public String getForm(Model model, User user) {
        model.addAttribute("fragment", "inscription");
        return "index";
    }

    @PostMapping("/inscription")
    public String postForm(Model model,
                           @Valid @ModelAttribute(name = "user") User user,
                           BindingResult userBinding // le binding suit le ModelAttribute
    ) {


        Optional<User> optionalUser = userService.findByEmail(user.getEmail());
        if (!userBinding.hasErrors() && optionalUser.isEmpty()) {

            PasswordEncoderJava pwdEncoder = new PasswordEncoderJava();

            byte[] salt = pwdEncoder.generateSalt();
            user.setSalt(Base64.getEncoder().encodeToString(salt));

            byte[] pwd = pwdEncoder.generatePassword(user.getMdp(), salt);
            user.setMdpHash(Base64.getEncoder().encodeToString(pwd));
            user.setRole(RoleEnum.ADMIN);

            userService.save(user);
            return "redirect:/";
        }
        if (optionalUser.isPresent()) {
            model.addAttribute("emailExist", "Un compte existe déjà à cette adresse");
        }

        model.addAttribute("fragment", "inscription");
        return "index";
    }


    //login

    @GetMapping("/login")
    public String getLogin(Model model, User user) {

        model.addAttribute("fragment", "login");
        return "index";
    }

    @PostMapping("/login")
    public String postLogin(Model model,
                            @Valid @ModelAttribute(name = "user") User user,
                            BindingResult userBinding,
                            HttpSession session
    ) {

        if (!userBinding.hasErrors()) {
            Optional<User> u = userService.findByEmail(user.getEmail());

            if (u.isPresent()) {
                PasswordEncoderJava pwdEncoder = new PasswordEncoderJava();
                byte[] h = pwdEncoder.generatePassword(user.getMdp(), Base64.getDecoder().decode(u.get().getSalt()));
                if (Base64.getEncoder().encodeToString(h).equals(u.get().getMdpHash())) {
                    session.setAttribute("user", u.get());
                    return "redirect:/";
                }
            }
            model.addAttribute("accountError", "Email ou le mdp incorrect");
        }
        model.addAttribute("fragment", "login");
        return "index";

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.setAttribute("user", null);
        //    session.invalidate(); je supprime la session
        return "index";
    }

}


//    @GetMapping("/")
//    @ResponseBody // je force pour envoyer un string et une page html
//    public String index() {
//        return "Coucou voici ma page Spring";
//    }

//    @GetMapping("/video")
//    public  String video() {
//        return "index";
//    }
//
//    // /{name} c'est un parametre
//    @GetMapping("/html")
//
//    autre façon de recuperer et passer de parametres
//    public String indexHtml(
//            @RequestParam(name = "firstName", defaultValue = "") String name,
//            @RequestParam(defaultValue = "0") Integer age,
//            @RequestParam(name = "header", defaultValue = "header1") String whatHeader,
//            Model model
//    ) {
//
//        String[] list = new String[]{
//                "hello",
//                "World",
//                "je m'apelle Erick",
//                "Je suis en formation chez M2i"
//        };
//        model.addAttribute("listes", list);
//        model.addAttribute("whatHeader", whatHeader);
//        model.addAttribute("firstName", name);
//        model.addAttribute("age", age);
//        return "index";
//    }
//
//    @PostMapping("/form")
//    public String search(
//            @RequestParam String search
//    ) {
//        System.out.println(search);
//        return "redirect:/";
//    }


