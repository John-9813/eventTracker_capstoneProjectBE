package johnoliveira.eventTracker_capstoneProject.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    // ricerca di un singolo evento specifico tramite il suo ID
    public EventDTO getEventById(String eventId) {
        Event event = eventRepository.findById(UUID.fromString(eventId)).orElseThrow(() ->
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
                event.getEventId().toString(),
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

    public List<EventDTO> parseResponseToEvents(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println("Risposta JSON completa: " + responseBody);

            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode eventsNode = rootNode.path("_embedded").path("events");
            if (!eventsNode.isArray()) {
                System.out.println("Nessun array di eventi trovato.");
                return Collections.emptyList();
            }

            System.out.println("Numero di eventi trovati: " + eventsNode.size());

            List<EventDTO> events = new ArrayList<>();
            for (JsonNode eventNode : eventsNode) {
                try {
                    EventDTO event = new EventDTO(
                            eventNode.path("id").asText("ID non disponibile"),
                            eventNode.path("name").asText("Titolo non disponibile"),
                            eventNode.path("description").asText("Descrizione non disponibile"),
                            eventNode.path("images").get(0).path("url").asText("https://via.placeholder.com/400x200"),
                            parseDate(eventNode.path("dates").path("start").path("localDate").asText("1900-01-01")),
                            parseDate(eventNode.path("dates").path("end").path("localDate").asText("1900-01-01")),
                            eventNode.path("_embedded").path("venues").get(0).path("name").asText("Luogo non disponibile"),
                            eventNode.path("url").asText("URL non disponibile"),
                            eventNode.path("classifications").get(0).path("segment").path("name").asText("Categoria non disponibile")
                    );
                    events.add(event);
                } catch (Exception e) {
                    System.err.println("Errore durante l'elaborazione di un evento: " + e.getMessage());
                }
            }

            return events;

        } catch (Exception e) {
            System.err.println("Errore durante l'analisi della risposta: " + e.getMessage());
            return Collections.emptyList();
        }
    }




    private LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString);
        } catch (Exception e) {
            return LocalDate.of(1900, 1, 1); // Valore di fallback
        }
    }

}

