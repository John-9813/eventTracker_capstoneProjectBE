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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ticketmaster.api.key}")
    private String apiKey;

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

    // metodo per la ricerca filtrati da l'api tramite parola chiave
    public List<EventDTO> searchEventsFromAPI(String keyword, String city) {
        // URL per Ticketmaster API con supporto per keyword e città
        String url = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=" + apiKey +
                "&keyword=" + keyword + "&city=" + city;

        try {
            // Effettua la richiesta all'API
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getBody() == null || response.getBody().isEmpty()) {
                System.out.println("La risposta del Ticketmaster API è vuota.");
                return Collections.emptyList();
            }

            // Parsiamo i risultati
            return parseResponseToEvents(response.getBody());
        } catch (Exception e) {
            System.err.println("Errore durante la ricerca eventi dall'API: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<EventDTO> filterEventsFromAPI(String keyword, String city, String category) {
        // Costruisci l'URL con i parametri dinamici
        StringBuilder urlBuilder = new StringBuilder("https://app.ticketmaster.com/discovery/v2/events.json?apikey=")
                .append(apiKey);

        if (!keyword.isEmpty()) {
            urlBuilder.append("&keyword=").append(keyword);
        }
        if (!city.isEmpty()) {
            urlBuilder.append("&city=").append(city);
        }
        if (!category.isEmpty()) {
            urlBuilder.append("&classificationName=").append(category);
        }

        try {
            // Effettua la richiesta all'API
            ResponseEntity<String> response = restTemplate.getForEntity(urlBuilder.toString(), String.class);
            if (response.getBody() == null || response.getBody().isEmpty()) {
                System.out.println("La risposta del Ticketmaster API è vuota.");
                return Collections.emptyList();
            }

            // Parsiamo i risultati
            return parseResponseToEvents(response.getBody());
        } catch (Exception e) {
            System.err.println("Errore durante il filtraggio eventi dall'API: " + e.getMessage());
            return Collections.emptyList();
        }
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
                event.getCategory().toString(),
                event.getCity()
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

            // Log della risposta completa
            System.out.println("Risposta JSON completa: " + responseBody);

            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode eventsNode = rootNode.path("_embedded").path("events");

            if (!eventsNode.isArray()) {
                // Log della risposta se non c'è un array di eventi
                System.out.println("Risposta completa (nessun array di eventi): " + responseBody);
                return Collections.emptyList();
            }

            List<EventDTO> events = new ArrayList<>();
            for (JsonNode eventNode : eventsNode) {
                try {
                    // Recupera le immagini disponibili
                    JsonNode imagesNode = eventNode.path("images");
                    String imageUrl = "https://via.placeholder.com/400x200";
                    boolean validImageFound = false;

                    if (imagesNode.isArray()) {
                        for (JsonNode image : imagesNode) {
                            int width = image.path("width").asInt(0);
                            int height = image.path("height").asInt(0);
                            if (width >= 800 && height >= 600) {
                                imageUrl = image.path("url").asText("https://via.placeholder.com/400x200");
                                validImageFound = true;
                                break;
                            }
                        }
                    }

                    if (!validImageFound) {
                        continue;
                    }

                    // Recupera la città (con controllo di sicurezza)
                    String city = "Città non disponibile";
                    JsonNode venuesNode = eventNode.path("_embedded").path("venues");
                    if (venuesNode.isArray() && venuesNode.size() > 0) {
                        city = venuesNode.get(0).path("city").path("name").asText("Città non disponibile");
                    }

                    // Recupera la categoria (con controllo di sicurezza)
                    String category = "Categoria non disponibile";
                    JsonNode classificationsNode = eventNode.path("classifications");
                    if (classificationsNode.isArray() && classificationsNode.size() > 0) {
                        category = classificationsNode.get(0).path("genre").path("name").asText("Categoria non disponibile");
                    }

                    // Creazione dell'oggetto EventDTO
                    EventDTO event = new EventDTO(
                            eventNode.path("id").asText("ID non disponibile"),
                            eventNode.path("name").asText("Titolo non disponibile"),
                            eventNode.path("description").asText("Descrizione non disponibile"),
                            imageUrl,
                            parseDate(eventNode.path("dates").path("start").path("localDate").asText("1900-01-01")),
                            parseDate(eventNode.path("dates").path("end").path("localDate").asText("1900-01-01")),
                            eventNode.path("_embedded").path("venues").get(0).path("name").asText("Luogo non disponibile"),
                            eventNode.path("url").asText("URL non disponibile"),
                            category,
                            city
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

