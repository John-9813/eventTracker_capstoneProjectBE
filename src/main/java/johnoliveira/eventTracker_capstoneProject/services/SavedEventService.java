package johnoliveira.eventTracker_capstoneProject.services;


import johnoliveira.eventTracker_capstoneProject.entities.SavedEvent;
import johnoliveira.eventTracker_capstoneProject.entities.User;
import johnoliveira.eventTracker_capstoneProject.exceptions.NotFoundException;
import johnoliveira.eventTracker_capstoneProject.repositories.SavedEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SavedEventService {

    @Autowired
    private SavedEventRepository savedEventRepository;

    public SavedEvent saveEvent(SavedEvent savedEvent) {
        return savedEventRepository.save(savedEvent);
    }

    public List<SavedEvent> getSavedEventsByUser(User user) {
        return savedEventRepository.findByUser(user);
    }

    public void deleteSavedEvent(UUID savedEventId) {
        SavedEvent savedEvent = savedEventRepository.findById(savedEventId)
                .orElseThrow(() -> new NotFoundException("Saved event not found with ID: " + savedEventId));
        savedEventRepository.delete(savedEvent);
    }
}

