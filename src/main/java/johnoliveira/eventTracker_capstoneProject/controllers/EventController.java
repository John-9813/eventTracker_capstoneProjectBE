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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
    private EventCache eventCache;

    @Autowired
    private RestTemplate restTemplate;


    @Value("${ticketmaster.api.key}")
    private String apiKey;


    @GetMapping("/proxy")
    public ResponseEntity<List<EventDTO>> proxyTicketmasterEvents(
            @RequestParam(required = false, defaultValue = "") String city,
            @RequestParam(defaultValue = "IT") String countryCode,
            @RequestParam(defaultValue = "it-it") String locale) {

        // Usa un valore di fallback per city se vuoto o non valido
        if (city.isEmpty() || city.equals("IT")) {
            city = "Milano"; // Valore di default
        }

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
            e.printStackTrace();
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
     * Cerca eventi salvati tramite parola chiave.
     * Richiesta GET:
     * /events/search?keyword=parola_chiave&page=0&size=10
     */
    @GetMapping("/search")
    public ResponseEntity<Page<EventDTO>> searchEventsByKeyword(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(eventService.searchEventsByKeyword(keyword, pageable));
    }

    /**
     * Recupera eventi salvati per categoria.
     * Richiesta GET:
     * /events/category/{category}?page=0&size=10
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<EventDTO>> getEventsByCategory(
            @PathVariable Category category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(eventService.getEventsByCategory(category, pageable));
    }
}


