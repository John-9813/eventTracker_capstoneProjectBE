package johnoliveira.eventTracker_capstoneProject.controllers;

import johnoliveira.eventTracker_capstoneProject.dto.EventCreateDTO;
import johnoliveira.eventTracker_capstoneProject.dto.EventDTO;
import johnoliveira.eventTracker_capstoneProject.enums.Category;
import johnoliveira.eventTracker_capstoneProject.services.EventService;
import johnoliveira.eventTracker_capstoneProject.tools.EventCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ticketmaster.api.key}")
    private String apiKey;

    /**
     * Proxy per recuperare eventi da Ticketmaster.
     * Richiesta GET:
     * /events/proxy
     */
    @GetMapping("/proxy")
    public ResponseEntity<List<EventDTO>> proxyTicketmasterEvents(
            @RequestParam(required = false, defaultValue = "Milano") String city,
            @RequestParam(defaultValue = "IT") String countryCode,
            @RequestParam(defaultValue = "it-it") String locale) {

        String url = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=" + apiKey +
                "&countryCode=" + countryCode + "&locale=" + locale +
                "&city=" + city;

        System.out.println("Invio richiesta a URL: " + url);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getBody() == null || response.getBody().isEmpty()) {
                System.err.println("La risposta del Ticketmaster API è vuota.");
                return ResponseEntity.ok(Collections.emptyList());
            }

            List<EventDTO> events = eventService.parseResponseToEvents(response.getBody());
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            System.err.println("Errore nella richiesta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Recupera tutti gli eventi salvati nel database con paginazione.
     * Richiesta GET:
     * /events?page=0&size=10
     */
    @GetMapping
    public ResponseEntity<Page<EventDTO>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(eventService.getAllEvents(pageable));
    }

    /**
     * Recupera un evento salvato tramite il suo ID.
     * Richiesta GET:
     * /events/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable String id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    /**
     * Crea un nuovo evento personalizzato.
     * Richiesta POST:
     * /events
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDTO createEvent(@RequestBody EventCreateDTO dto) {
        return eventService.createEvent(dto);
    }

    /**
     * Filtro avanzato per eventi con supporto per keyword e city.
     * Richiesta GET:
     * /events/filter?keyword=parola_chiave&city=nome_città&page=0&size=20
     */
    @GetMapping("/filter")
    public ResponseEntity<List<EventDTO>> filterEvents(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        // Evita chiamate con parametri vuoti
        if ((keyword == null || keyword.isEmpty()) && (city == null || city.isEmpty())) {
            System.out.println("Chiamata con parametri vuoti, nessun evento filtrato.");
            return ResponseEntity.ok(Collections.emptyList());
        }

        StringBuilder urlBuilder = new StringBuilder("https://app.ticketmaster.com/discovery/v2/events.json?apikey=")
                .append(apiKey)
                .append("&countryCode=IT&locale=it-it")
                .append("&page=").append(page)
                .append("&size=").append(size);

        if (keyword != null && !keyword.isEmpty()) {
            urlBuilder.append("&keyword=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8));
        }
        if (city != null && !city.isEmpty()) {
            urlBuilder.append("&city=").append(URLEncoder.encode(city, StandardCharsets.UTF_8));
        }

        System.out.println("URL Ticketmaster generato: " + urlBuilder.toString());

        try {
            String responseBody = restTemplate.getForObject(urlBuilder.toString(), String.class);
            List<EventDTO> events = eventService.parseResponseToEvents(responseBody);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            System.err.println("Errore durante il filtraggio degli eventi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}



