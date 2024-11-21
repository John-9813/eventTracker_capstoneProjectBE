package johnoliveira.eventTracker_capstoneProject.controllers;


import johnoliveira.eventTracker_capstoneProject.entities.User;
import johnoliveira.eventTracker_capstoneProject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Crea un nuovo utente
     * richiesta POST:
     * URL: /users
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public void createUser(@RequestBody User user) {
        userService.createUser(user.getName(), user.getSurname(), user.getPassword(), user.getEmail());
    }

    /**
     * recupera un utente tramite il suo ID
     * richiesta GET:
     * URL: /users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * elimina un utente tramite il suo ID
     * richiesta DELETE:
     * URL: /users/{id}
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }
}



