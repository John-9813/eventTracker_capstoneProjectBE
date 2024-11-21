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

    // metodo per salvare un evento
    public SavedEvent saveEvent(SavedEvent savedEvent) {
        return savedEventRepository.save(savedEvent);
    }

    // metodo per mostrare gli eventi salvati
    public List<SavedEvent> getSavedEventsByUser(User user) {
        return savedEventRepository.findByUser(user);
    }

    // metodo per eliminare eventi salvati
    public void deleteSavedEvent(UUID savedEventId) {
        SavedEvent savedEvent = savedEventRepository.findById(savedEventId).orElseThrow(() ->
                        new NotFoundException("Saved event not found with ID: " + savedEventId));
        savedEventRepository.delete(savedEvent);
    }

    // metodo per aggiornare annotazioni sugli eventi salvati
    public SavedEvent updateNotes(UUID savedEventId, String newNotes) {
        SavedEvent savedEvent = savedEventRepository.findById(savedEventId).orElseThrow(() ->
                        new NotFoundException("Saved event not found with ID: " + savedEventId));
        savedEvent.setNotes(newNotes);
        return savedEventRepository.save(savedEvent);
    }

}

