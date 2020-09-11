package com.formation.spring_07_sep.controllers.admin;


import com.formation.spring_07_sep.models.Article;
import com.formation.spring_07_sep.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.*;
import java.util.Optional;

@Controller
@RequestMapping("/admin/articles")
public class AdminArticleController {

    @Autowired
    private final ArticleService articleService;

    public AdminArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("")
    private String index(Model model) {
        model.addAttribute("articles", articleService.findAll());
        model.addAttribute("fragment", "article/index");
        return "admin/index";
    }

    @GetMapping("{ref}")
    public String oneArticle(
            @PathVariable Long ref,
            @RequestParam String action, // recuperer l'action cad supprimer
            Model model
    ) {
        Optional<Article> article = articleService.findById(ref);
        if (action.equals("delete")) {

            if (article.isPresent()) {
                // supprimer les images de bdd
//                File file = new File("src/main/resources/static/img"+article.get().getLink());
//                file.delete();
                articleService.delete(ref);
            }

            return "redirect:/admin/articles";
        }

        if (action.equals("update") && article.isPresent()) {
            model.addAttribute("article", article.get());
            model.addAttribute("action", "/" + ref + "?action=update");
            model.addAttribute("fragment", "article/form");

        }
        return "admin/index";
    }

    // Methode pour ajouter un article
    @GetMapping("add")
    public String getCreateArticle(Model model, Article article) {
        model.addAttribute("action", "/add");
        model.addAttribute("fragment", "article/form");
        return "admin/index";
    }


    @PostMapping("add")
    public String postCreateArticle(
            Model model,
            @Valid @ModelAttribute(name = "article") Article article,
            BindingResult articleBinding,
            @RequestParam(name = "image") MultipartFile image,
            RedirectAttributes attributes
    ){
        if (!articleBinding.hasErrors()) {
            boolean isValid = true;
            if (!image.isEmpty() && image.getContentType().equals("image/jpeg")
                    || image.getContentType().equals("image/webp")
                    || image.getContentType().equals("image/heic")
                    || image.getContentType().equals("image/png")
            ) {
                File img = new File("src/main/resources/static/img/" + image.getOriginalFilename());
                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(img))) {
                    bos.write(image.getBytes());
                    article.setLink(image.getOriginalFilename());
                } catch (IOException e) {
                    isValid = false;
                    model.addAttribute("action", "/add");
                    model.addAttribute("fragment", "article/form");
                    model.addAttribute("errorMessage", "Un problème de sauvegarde est survenu");
                }
            }

            if (isValid) {
                articleService.save(article);
                attributes.addFlashAttribute("message", "L'article " + article.getTitle() + " a bien été enregistré");
                return "redirect:/admin/articles";
            }
        }
        model.addAttribute("action", "/add");
        model.addAttribute("fragment", "article/form");
        return "/admin/index";
    }

    @PostMapping("{ref}")
    public String update(
            @Valid @ModelAttribute(name = "article") Article article,
            BindingResult articleBinding,
            @PathVariable Long ref,
            @RequestParam String action,
            @RequestParam(name = "image") MultipartFile image,
            Model model,
            RedirectAttributes attributes
    ) {
        if (!articleBinding.hasErrors()) {

            boolean isValid = true;

            if (!image.isEmpty() && image.getContentType().equals("image/jpeg")
                    || image.getContentType().equals("image/webp")
                    || image.getContentType().equals("image/heic")
                    || image.getContentType().equals("image/png")
            ) {

                File img = new File("src/main/resources/static/img/" + image.getOriginalFilename());
                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(img))) {
                    bos.write(image.getBytes());
                    if (new File("src/main/resources/static/img/" + article.getLink()).delete()) {
                        article.setLink(image.getOriginalFilename());
                    }
                } catch (IOException e) {
                    isValid = false;
                    model.addAttribute("action", "/" + ref + "?action=update");
                    model.addAttribute("fragment", "article/form");
                    model.addAttribute("errorMessage", "Un problème de sauvegarde est survenu");
                }
            }

            if (isValid) {
                articleService.save(article);
                attributes.addFlashAttribute("message", "L'article " + ref + " a bien été mis à jour");
                return "redirect:/admin/articles";
            }
        }
        model.addAttribute("action", "/" + ref + "?action=update");
        model.addAttribute("fragment", "article/form");
        return "/admin/index";
    }

}
