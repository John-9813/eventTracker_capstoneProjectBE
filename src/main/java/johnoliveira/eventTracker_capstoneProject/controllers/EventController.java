package johnoliveira.eventTracker_capstoneProject.controllers;

import johnoliveira.eventTracker_capstoneProject.dto.EventCreateDTO;
import johnoliveira.eventTracker_capstoneProject.dto.EventDTO;
import johnoliveira.eventTracker_capstoneProject.enums.Category;
import johnoliveira.eventTracker_capstoneProject.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;



@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

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
    public ResponseEntity<EventDTO> getEventById(@PathVariable UUID id) {
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


