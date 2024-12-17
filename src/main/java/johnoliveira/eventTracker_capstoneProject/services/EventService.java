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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    // ritorna tutti gli eventi con paginazione usando il DTO
    public Page<EventDTO> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable)
                .map(this::toEventDTO);
    }


    // metodo per la ricerca filtrata degli eventi tramite parola chiave con paginazione
    public Page<EventDTO> searchEventsByKeyword(String keyword, Pageable pageable) {
        return eventRepository.findByTitleContainingIgnoreCase(keyword, pageable).map(this::toEventDTO);
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

    public List<EventDTO> filterEventsFromAPI(String keyword, String city, String classificationName, int page, int size) {
        StringBuilder urlBuilder = new StringBuilder("https://app.ticketmaster.com/discovery/v2/events.json?apikey=")
                .append(apiKey)
                .append("&page=").append(page)
                .append("&size=").append(size);

        if (keyword != null && !keyword.isEmpty()) {
            urlBuilder.append("&keyword=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8));
        }
        if (city != null && !city.isEmpty()) {
            urlBuilder.append("&city=").append(URLEncoder.encode(city, StandardCharsets.UTF_8));
        }
        if (classificationName != null && !classificationName.isEmpty()) {
            urlBuilder.append("&classificationName=").append(URLEncoder.encode(classificationName, StandardCharsets.UTF_8));
        }

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(urlBuilder.toString(), String.class);
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
                event.getImageUrl() != null ? event.getImageUrl() : "https://via.placeholder.com/400x200",
                event.getStartDate(),
                event.getStartTime() != null ? event.getStartTime() : "Orario non disponibile",
                event.getLocation(),
                event.getPageUrl(),
                event.getTicketStatus() != null ? event.getTicketStatus() : "Stato non disponibile",
                event.getCity()
        );
    }



    // metodo per creare eventi per test
    public EventDTO createEvent(EventCreateDTO dto) {
        Event event = new Event();
        event.setTitle(dto.title());
        event.setImageUrl(dto.imageUrl());
        event.setStartDate(dto.startDate());
        event.setStartTime(dto.startTime());
        event.setEndDate(dto.endDate());
        event.setLocation(dto.location());
        event.setPageUrl(dto.pageUrl());
        event.setTicketStatus(dto.ticketStatus());
        event.setCity(dto.city());

        return toEventDTO(eventRepository.save(event));
    }


    public List<EventDTO> parseResponseToEvents(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode eventsNode = rootNode.path("_embedded").path("events");

            if (!eventsNode.isArray()) {
                return Collections.emptyList();
            }

            List<EventDTO> events = new ArrayList<>();
            for (JsonNode eventNode : eventsNode) {
                try {
                    // ID e titolo evento
                    String id = eventNode.path("id").asText("ID non disponibile");
                    String title = eventNode.path("name").asText("Titolo non disponibile");

                    // URL immagine principale
                    String imageUrl = "https://via.placeholder.com/400x200";
                    JsonNode imagesNode = eventNode.path("images");
                    if (imagesNode.isArray()) {
                        for (JsonNode image : imagesNode) {
                            int width = image.path("width").asInt(0);
                            int height = image.path("height").asInt(0);
                            if (width >= 800 && height >= 600) {
                                imageUrl = image.path("url").asText("https://via.placeholder.com/400x200");
                                break;
                            }
                        }
                    }

                    // Data e ora evento
                    String startDate = eventNode.path("dates").path("start").path("localDate").asText("01-01-1990");
                    String startTime = eventNode.path("dates").path("start").path("localTime").asText("Orario non disponibile");

                    // Stato biglietti
                    String ticketStatus = eventNode.path("dates")
                            .path("status")
                            .path("code")
                            .asText(eventNode.path("sales")
                                    .path("public")
                                    .path("startDateTime").isMissingNode()
                                    ? "Esauriti"
                                    : "Disponibili");

                    // Informazioni location e città
                    String venueName = "Luogo non disponibile";
                    String city = "Città non disponibile";
                    JsonNode venuesNode = eventNode.path("_embedded").path("venues");
                    if (venuesNode.isArray() && venuesNode.size() > 0) {
                        venueName = venuesNode.get(0).path("name").asText("Luogo non disponibile");
                        city = venuesNode.get(0).path("city").path("name").asText("Città non disponibile");
                    }

                    // URL per acquisto biglietti
                    String pageUrl = eventNode.path("url").asText("URL non disponibile");

                    // Creazione EventDTO
                    EventDTO event = new EventDTO(
                            id,
                            title,
                            imageUrl,
                            LocalDate.parse(startDate),
                            startTime,
                            venueName,
                            pageUrl,
                            ticketStatus,
                            city
                    );

                    events.add(event);
                } catch (Exception e) {
                    System.err.println("Errore durante l'elaborazione di un evento: " + e.getMessage());
                }
            }

            return events;

        } catch (Exception e) {
            System.err.println("Errore durante l'analisi della risposta JSON: " + e.getMessage());
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

