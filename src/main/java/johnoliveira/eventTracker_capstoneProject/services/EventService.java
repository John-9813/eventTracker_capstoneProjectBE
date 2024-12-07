package johnoliveira.eventTracker_capstoneProject.services;

import johnoliveira.eventTracker_capstoneProject.dto.EventCreateDTO;
import johnoliveira.eventTracker_capstoneProject.dto.EventDTO;
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
    public EventDTO getEventById(UUID eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event not found with ID: " + eventId));
        return toEventDTO(event);
    }

    // ricerca degli eventi tramite categoria con paginazione
    public Page<EventDTO> getEventsByCategory(Category category, Pageable pageable) {
        return eventRepository.findByCategory(category, pageable).map(this::toEventDTO);
    }

    // ritorna tutti gli eventi con paginazione usando il DTO
    public Page<EventDTO> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable)
                .map(this::toEventDTO);
    }


    // metodo per la ricerca filtrata degli eventi tramite parola chiave con paginazione
    public Page<EventDTO> searchEventsByKeyword(String keyword, Pageable pageable) {
        return eventRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword,keyword, pageable).map(this::toEventDTO);
    }

    // mapping del DTO
    private EventDTO toEventDTO(Event event) {
        return new EventDTO(
                event.getEventId(),
                event.getTitle(),
                event.getDescription(),
                event.getImageUrl(),
                event.getStartDate(),
                event.getEndDate(),
                event.getLocation(),
                event.getPageUrl(),
                event.getCategory().toString()
        );
    }

    // metodo per creare eventi per test
    public EventDTO createEvent(EventCreateDTO dto) {
        Event event = new Event();
        event.setTitle(dto.title());
        event.setDescription(dto.description());
        event.setImageUrl(dto.imageUrl());
        event.setStartDate(dto.startDate());
        event.setEndDate(dto.endDate());
        event.setLocation(dto.location());
        event.setPageUrl(dto.pageUrl());
        event.setCategory(Category.valueOf(dto.category().toUpperCase()));
        return toEventDTO(eventRepository.save(event));
    }

}

