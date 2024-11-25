package johnoliveira.eventTracker_capstoneProject.services;


import johnoliveira.eventTracker_capstoneProject.dto.SavedEventCreateDTO;
import johnoliveira.eventTracker_capstoneProject.dto.SavedEventDTO;
import johnoliveira.eventTracker_capstoneProject.entities.Event;
import johnoliveira.eventTracker_capstoneProject.entities.SavedEvent;
import johnoliveira.eventTracker_capstoneProject.entities.User;
import johnoliveira.eventTracker_capstoneProject.exceptions.NotFoundException;
import johnoliveira.eventTracker_capstoneProject.repositories.EventRepository;
import johnoliveira.eventTracker_capstoneProject.repositories.SavedEventRepository;
import johnoliveira.eventTracker_capstoneProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class SavedEventService {

    @Autowired
    private SavedEventRepository savedEventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    // metodo per salvare un evento
    public SavedEventDTO saveEvent(SavedEventCreateDTO dto) {
        User user = userRepository.findById(dto.userId()).orElseThrow(() ->
                new NotFoundException("User not found with ID: " + dto.userId()));

        Event event = eventRepository.findById(dto.eventId()).orElseThrow(() ->
                new NotFoundException("Event not found with ID: " + dto.eventId()));

        SavedEvent savedEvent = new SavedEvent();
        savedEvent.setUser(user);
        savedEvent.setEvent(event);
        savedEvent.setNotes(dto.notes());
        savedEvent.setSavedAt(LocalDate.now());

        return toSavedEventDTO(savedEventRepository.save(savedEvent));
    }

    // metodo per mostrare gli eventi salvati
    public List<SavedEventDTO> getSavedEventsByUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User not found with ID: " + userId));

        return savedEventRepository.findByUser(user)
                .stream()
                .map(this::toSavedEventDTO)
                .toList();
    }

    // metodo per eliminare eventi salvati
    public void deleteSavedEvent(UUID savedEventId) {
        if (!savedEventRepository.existsById(savedEventId)) {
            throw new NotFoundException("Saved event not found with ID: " + savedEventId);
        }
        savedEventRepository.deleteById(savedEventId);
    }

    // metodo per aggiornare annotazioni sugli eventi salvati
    public SavedEventDTO updateNotes(UUID savedEventId, String newNotes) {
        SavedEvent savedEvent = savedEventRepository.findById(savedEventId)
                .orElseThrow(() -> new NotFoundException("Saved event not found with ID: " + savedEventId));
        savedEvent.setNotes(newNotes);
        savedEventRepository.save(savedEvent);
        return toSavedEventDTO(savedEvent);
    }

    // mapping del DTO
    private SavedEventDTO toSavedEventDTO(SavedEvent savedEvent) {
        return new SavedEventDTO(
                savedEvent.getSavedEventId(),
                savedEvent.getUser().getUserId(),
                savedEvent.getEvent().getEventId(),
                savedEvent.getNotes(),
                savedEvent.getSavedAt()
        );
    }

}

