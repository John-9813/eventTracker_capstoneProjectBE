package johnoliveira.eventTracker_capstoneProject.services;


import johnoliveira.eventTracker_capstoneProject.entities.News;
import johnoliveira.eventTracker_capstoneProject.exceptions.NotFoundException;
import johnoliveira.eventTracker_capstoneProject.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    // metodo per cercare news
    public News getNewsById(UUID newsId) {
        return newsRepository.findById(newsId).orElseThrow(() ->
                new NotFoundException("News not found with ID: " + newsId));
    }

    //metodo per ottenere la lista di tutte le news
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    // metodo per cercare le news tramite keyword specifiche
    public List<News> searchNewsByKeyword(String keyword) {
        return newsRepository.findAll().stream().filter(news ->
                        news.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        news.getDescription().toLowerCase().contains(keyword.toLowerCase())).toList();
    }

}

