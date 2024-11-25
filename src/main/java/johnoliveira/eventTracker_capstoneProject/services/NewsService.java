package johnoliveira.eventTracker_capstoneProject.services;


import johnoliveira.eventTracker_capstoneProject.dto.EventDTO;
import johnoliveira.eventTracker_capstoneProject.dto.NewsDTO;
import johnoliveira.eventTracker_capstoneProject.entities.Event;
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
    public NewsDTO getNewsById(UUID newsId) {
        News news = newsRepository.findById(newsId).orElseThrow(() ->
                new NotFoundException("News not found with ID: " + newsId));
        return toNewsDTO(news);
    }


    // recupera tutte le notizie con paginazione.
    public Page<NewsDTO> getAllNews(Pageable pageable) {
        return newsRepository.findAll(pageable).map(this::toNewsDTO);
    }


    // cerca notizie tramite parola chiave con paginazione.
    public Page<NewsDTO> searchNewsByKeyword(String keyword, Pageable pageable) {
        return newsRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                keyword, keyword, pageable).map(this::toNewsDTO);
    }

    // mapping del DTO
    private NewsDTO toNewsDTO(News news) {
        return new NewsDTO(
                news.getNewsId(),
                news.getTitle(),
                news.getDescription(),
                news.getImageUrl(),
                news.getPublishedDate(),
                news.getSource(),
                news.getUrl()
        );
    }
}


