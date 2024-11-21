package johnoliveira.eventTracker_capstoneProject.services;


import johnoliveira.eventTracker_capstoneProject.entities.News;
import johnoliveira.eventTracker_capstoneProject.exceptions.NotFoundException;
import johnoliveira.eventTracker_capstoneProject.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;


    // recupera una notizia tramite il suo ID.
    public News getNewsById(UUID newsId) {
        return newsRepository.findById(newsId).orElseThrow(() ->
                new NotFoundException("News not found with ID: " + newsId));
    }


    // recupera tutte le notizie con paginazione.
    public Page<News> getAllNews(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }


    // cerca notizie tramite parola chiave con paginazione.
    public Page<News> searchNewsByKeyword(String keyword, Pageable pageable) {
        return newsRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                keyword, keyword, pageable);
    }
}


