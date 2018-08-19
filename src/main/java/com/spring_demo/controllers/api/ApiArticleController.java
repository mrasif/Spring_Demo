package com.spring_demo.controllers.api;

import com.spring_demo.models.Article;
import com.spring_demo.models.JSONResponse;
import com.spring_demo.repositories.ArticleRepository;
import com.spring_demo.services.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class ApiArticleController {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/articles")
    public ResponseEntity getAll(@RequestHeader("access-token") String token){
        JSONResponse jsonResponse=new JSONResponse();
        if (jwtService.isAuthenticated(token)) {
            List<Article> articles = articleRepository.findAll();
            jsonResponse.addBody("articles", articles);
        }
        else {
            jsonResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
        }
        return jsonResponse.toResponseEntity();
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity getArticle(@RequestHeader("access-token") String token, @PathVariable("id") long id){
        JSONResponse jsonResponse=new JSONResponse();
        if (jwtService.isAuthenticated(token)) {
            Article article = articleRepository.findById(id).isPresent() ? articleRepository.findById(id).get() : null;
            jsonResponse.addBody("article", article);
        }
        else {
            jsonResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
        }
        return jsonResponse.toResponseEntity();
    }

    @PatchMapping("articles/{id}")
    public ResponseEntity updateArticle(@RequestHeader("access-token") String token, @RequestBody Article article, @PathVariable("id") long id){
        JSONResponse jsonResponse=new JSONResponse();
        if (jwtService.isAuthenticated(token)) {
            if (articleRepository.findById(id).isPresent()) {
                articleRepository.save(article);
                jsonResponse.addBody("article", article);
                jsonResponse.addBody("message", "Article updated.");
            } else {
                jsonResponse.addBody("message", "Article failed to update.");
                jsonResponse.setHttpStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        else {
            jsonResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
        }
        return jsonResponse.toResponseEntity();
    }
}
