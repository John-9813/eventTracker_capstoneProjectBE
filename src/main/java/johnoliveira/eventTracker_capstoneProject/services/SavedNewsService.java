package johnoliveira.eventTracker_capstoneProject.services;


import johnoliveira.eventTracker_capstoneProject.entities.SavedNews;
import johnoliveira.eventTracker_capstoneProject.entities.User;
import johnoliveira.eventTracker_capstoneProject.exceptions.NotFoundException;
import johnoliveira.eventTracker_capstoneProject.repositories.SavedNewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SavedNewsService {

    @Autowired
    private SavedNewsRepository savedNewsRepository;

    public SavedNews saveNews(SavedNews savedNews) {
        return savedNewsRepository.save(savedNews);
    }

    public List<SavedNews> getSavedNewsByUser(User user) {
        return savedNewsRepository.findByUser(user);
    }

    public void deleteSavedNews(UUID savedNewsId) {
        SavedNews savedNews = savedNewsRepository.findById(savedNewsId).orElseThrow(() ->
                        new NotFoundException("Saved news not found with ID: " + savedNewsId));
        savedNewsRepository.delete(savedNews);
    }
}

