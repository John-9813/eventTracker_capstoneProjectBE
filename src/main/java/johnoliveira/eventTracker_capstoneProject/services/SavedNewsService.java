package johnoliveira.eventTracker_capstoneProject.services;


import johnoliveira.eventTracker_capstoneProject.dto.SavedNewsCreateDTO;
import johnoliveira.eventTracker_capstoneProject.dto.SavedNewsDTO;
import johnoliveira.eventTracker_capstoneProject.entities.News;
import johnoliveira.eventTracker_capstoneProject.entities.SavedNews;
import johnoliveira.eventTracker_capstoneProject.entities.User;
import johnoliveira.eventTracker_capstoneProject.exceptions.NotFoundException;
import johnoliveira.eventTracker_capstoneProject.repositories.NewsRepository;
import johnoliveira.eventTracker_capstoneProject.repositories.SavedNewsRepository;
import johnoliveira.eventTracker_capstoneProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class SavedNewsService {

    @Autowired
    private SavedNewsRepository savedNewsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NewsRepository newsRepository;

    // metodo per salvare una notizia
    public SavedNewsDTO saveNews(SavedNewsCreateDTO dto) {
        User user = userRepository.findById(dto.userId()).orElseThrow(() ->
                new NotFoundException("User not found with ID: " + dto.userId()));

        News news = newsRepository.findById(dto.newsId()).orElseThrow(() ->
                new NotFoundException("News not found with ID: " + dto.newsId()));

        SavedNews savedNews = new SavedNews();
        savedNews.setUser(user);
        savedNews.setNews(news);
        savedNews.setSavedAt(LocalDate.now());

        return toSavedNewsDTO(savedNewsRepository.save(savedNews));
    }

    // metodo per mostrare le notizie salvati
    public List<SavedNewsDTO> getSavedNewsByUser(UUID userId) {
       User user = userRepository.findById(userId).orElseThrow(() ->
               new NotFoundException("User not found with ID: " + userId));

       return savedNewsRepository.findByUser(user)
               .stream()
               .map(this::toSavedNewsDTO)
               .toList();
    }

    // metodo per eliminare notizie salvate
    public void deleteSavedNews(UUID savedNewsId) {
        if (!savedNewsRepository.existsById(savedNewsId)) {
            throw new NotFoundException("Saved news not found with ID: " + savedNewsId);
        }
        savedNewsRepository.deleteById(savedNewsId);
    }

    // mapping del DTO
    private SavedNewsDTO toSavedNewsDTO(SavedNews savedNews){
        return new SavedNewsDTO(
                savedNews.getSavedNewsId(),
                savedNews.getUser().getUserId(),
                savedNews.getNews().getNewsId(),
                savedNews.getSavedAt()
        );
    }
}

