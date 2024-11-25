package johnoliveira.eventTracker_capstoneProject.controllers;

import johnoliveira.eventTracker_capstoneProject.dto.NewsCreateDTO;
import johnoliveira.eventTracker_capstoneProject.dto.NewsDTO;
import johnoliveira.eventTracker_capstoneProject.entities.News;
import johnoliveira.eventTracker_capstoneProject.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.UUID;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    /**
     * Recupera tutte le notizie e le inpagina
     * richiesta GET:
     * URL: /news?page=0&size=10
     */
    @GetMapping
    public ResponseEntity<Page<NewsDTO>> getAllNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(newsService.getAllNews(pageable));
    }

    /**
     * recupera una notizia tramite il suo ID
     * richiesta GET:
     * URL: /news/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> getNewsById(@PathVariable UUID id) {
        return ResponseEntity.ok(newsService.getNewsById(id));
    }

    /**
     * cerca notizie tramite parola chiave
     * richiesta esempio GET:
     * URL: /news/search?keyword=parola_chiave&page=0&size=10
     */
    @GetMapping("/search")
    public ResponseEntity<Page<NewsDTO>> searchNewsByKeyword(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(newsService.searchNewsByKeyword(keyword, pageable));
    }

    // richiesta per la creazione di news
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public NewsDTO createNews(@RequestBody NewsCreateDTO dto) {
        return newsService.createNews(dto);
    }

}

