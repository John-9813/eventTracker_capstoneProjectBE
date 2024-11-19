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

    public News getNewsById(UUID newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new NotFoundException("News not found with ID: " + newsId));
    }

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }
}

