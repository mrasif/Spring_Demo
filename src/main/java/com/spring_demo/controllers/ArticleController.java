package com.spring_demo.controllers;

import com.spring_demo.models.Article;
import com.spring_demo.models.User;
import com.spring_demo.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/articles")
    public List<Article> index(){
        List<Article> articles=articleRepository.findAll();
        return articles;
    }

    @RequestMapping("/articles/new")
    public ModelAndView _new(){

        ModelAndView mav=new ModelAndView();
        mav.setViewName("articles/new");
        return mav;
    }

    @PostMapping("/articles")
    public ModelAndView create(@RequestParam("title") String title, @RequestParam("body") String body){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user=(User) auth.getPrincipal();
        Article article=new Article();
        article.setTitle(title);
        article.setBody(body);
        Set<User> userSet=new HashSet<>();
        userSet.add(user);
        article.setUsers(userSet);
        articleRepository.save(article);
        ModelAndView mav=new ModelAndView();
        mav.setViewName("redirect:/articles");
        return mav;
    }
}
