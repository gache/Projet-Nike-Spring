package com.formation.spring_07_sep.controllers.admin;


import com.formation.spring_07_sep.models.Article;
import com.formation.spring_07_sep.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("{ref}")
    public String update(
            @ModelAttribute(name = "article") Article article,
            @PathVariable Long ref,
            @RequestParam String action,
            @RequestParam(name = "image") MultipartFile image,
            Model model
    ) {
        // Todo else si aucun chagement d'image alors remettre le leink précedent
        if (!image.isEmpty()) {
            if (image.getContentType().equals("image/jpeg")
                    || image.getContentType().equals("image/webp")
                    || image.getContentType().equals("image/heic")
                    || image.getContentType().equals("image/png")
            ) {
                //  Todo: Verifier la taille de l'image envoie enregistrer
                //  retourne un erreur au cas ou.
                article.setLink(image.getOriginalFilename());
                File img = new File("src/main/resources/static/img" + article.getLink());
                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(img))) {
                    bos.write(image.getBytes());
                    articleService.save(article);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                ;
            }
        }

        return "/admin/index";
    }

}
