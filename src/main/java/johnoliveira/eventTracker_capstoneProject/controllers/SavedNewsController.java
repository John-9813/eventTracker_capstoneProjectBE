package johnoliveira.eventTracker_capstoneProject.controllers;

import johnoliveira.eventTracker_capstoneProject.entities.SavedNews;
import johnoliveira.eventTracker_capstoneProject.entities.User;
import johnoliveira.eventTracker_capstoneProject.services.SavedNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/saved-news")
public class SavedNewsController {

    @Autowired
    private SavedNewsService savedNewsService;

    /**
     * Recupera le notizie salvate da un utente
     * Richiesta GET:
     * URL: /saved-news/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<SavedNews>> getSavedNewsByUser(@PathVariable UUID userId) {
        User user = new User();
        user.setUserId(userId);
        return ResponseEntity.ok(savedNewsService.getSavedNewsByUser(user));
    }

    /**
     * salva una nuova notizia per un utente
     * richiesta POST:
     * URL: /saved-news
     * body:
     * {
     *   "user": { "userId": "uuid" },
     *   "news": { "newsId": "uuid" }
     * }
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public void saveNews(@RequestBody SavedNews savedNews) {
        savedNewsService.saveNews(savedNews);
    }

    /**
     * elimina una notizia salvata
     * richiesta DELETE:
     * URL: /saved-news/{id}
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void deleteSavedNews(@PathVariable UUID id) {
        savedNewsService.deleteSavedNews(id);
    }
}



