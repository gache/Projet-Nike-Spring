package com.formation.spring_07_sep.services;

import com.formation.spring_07_sep.models.Article;
import com.formation.spring_07_sep.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ArticleService {

    private ArticleRepository articleRepository;

    @Autowired // Cette annotation  fait l'instance pour nous
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Collection<Article> findAll() {
        return articleRepository.findAll();
    }

    public void delete(Long ref) {
        articleRepository.deleteById(ref);
    }

    public Optional<Article> findById(Long ref) {
        return articleRepository.findById(ref);
    }

    public void save(Article article) {
        articleRepository.save(article);
    }
}
