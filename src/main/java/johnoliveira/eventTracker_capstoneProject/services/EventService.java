package johnoliveira.eventTracker_capstoneProject.services;

import johnoliveira.eventTracker_capstoneProject.entities.Event;
import johnoliveira.eventTracker_capstoneProject.enums.Category;
import johnoliveira.eventTracker_capstoneProject.exceptions.NotFoundException;
import johnoliveira.eventTracker_capstoneProject.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    // ricerca di singolo evento specifico
    public Event getEventById(UUID eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                        new NotFoundException("Event not found with ID: " + eventId));
    }

    // ricerca degli eventi tramite categoria
    public List<Event> getEventsByCategory(Category category) {
        return eventRepository.findByCategory(category.name());
    }

    // riporta la lista di tutti gli eventi
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // metodo per la ricerca filtrata degli eventi tramite keyword
    public List<Event> searchEventsByKeyword(String keyword) {
        return eventRepository.findAll().stream().filter(event ->
                        event.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        event.getDescription().toLowerCase().contains(keyword.toLowerCase())).toList();
    }

}
