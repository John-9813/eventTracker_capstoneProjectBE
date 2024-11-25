package johnoliveira.eventTracker_capstoneProject.controllers;

import johnoliveira.eventTracker_capstoneProject.dto.SavedEventCreateDTO;
import johnoliveira.eventTracker_capstoneProject.dto.SavedEventDTO;
import johnoliveira.eventTracker_capstoneProject.entities.SavedEvent;
import johnoliveira.eventTracker_capstoneProject.entities.User;
import johnoliveira.eventTracker_capstoneProject.services.SavedEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/saved-events")
public class SavedEventController {

    @Autowired
    private SavedEventService savedEventService;

    /**
     * Recupera gli eventi salvati da un utente
     * richiesta GET:
     * URL: /saved-events/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<SavedEventDTO>> getSavedEventsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(savedEventService.getSavedEventsByUser(userId));
    }

    /**
     * salva un nuovo evento per un utente
     * richiesta POST:
     * URL: /saved-events
     * Body di esempio:
     * {
     *   "user": { "userId": "uuid" },
     *   "event": { "eventId": "uuid" },
     *   "notes": "Ricordati di portare una giacca"
     * }
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public SavedEventDTO saveEvent(@RequestBody SavedEventCreateDTO dto) {
        return savedEventService.saveEvent(dto);
    }

    /**
     * Aggiorna le note di un evento salvato
     * Richiesta PUT:
     * URL: /saved-events/{id}
     * Body:
     * {
     *   "notes": "Nuove note aggiornate"
     * }
     */
    @PutMapping("/{id}")
    public ResponseEntity<SavedEventDTO> updateNotes(@PathVariable UUID id, @RequestBody String newNotes) {
        return ResponseEntity.ok(savedEventService.updateNotes(id, newNotes));
    }

    /**
     * Elimina un evento salvato.
     * Richiesta DELETE:
     * URL: /saved-events/{id}
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void deleteSavedEvent(@PathVariable UUID id) {
        savedEventService.deleteSavedEvent(id);
    }
}


