package johnoliveira.eventTracker_capstoneProject.services;

import johnoliveira.eventTracker_capstoneProject.entities.Event;
import johnoliveira.eventTracker_capstoneProject.enums.Category;
import johnoliveira.eventTracker_capstoneProject.exceptions.NotFoundException;
import johnoliveira.eventTracker_capstoneProject.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    // ricerca di un singolo evento specifico tramite il suo ID
    public Event getEventById(UUID eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event not found with ID: " + eventId));
    }


    // ricerca degli eventi tramite categoria con paginazione
    public Page<Event> getEventsByCategory(Category category, Pageable pageable) {
        return eventRepository.findByCategory(category.name(), pageable);
    }

    // ritorna tutti gli eventi con paginazione
    public Page<Event> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }


    // metodo per la ricerca filtrata degli eventi tramite parola chiave con paginazione
    public Page<Event> searchEventsByKeyword(String keyword, Pageable pageable) {
        return eventRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword,keyword, pageable);
    }
}

