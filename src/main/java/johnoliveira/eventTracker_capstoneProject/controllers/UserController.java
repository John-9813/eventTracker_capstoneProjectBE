package johnoliveira.eventTracker_capstoneProject.controllers;


import johnoliveira.eventTracker_capstoneProject.dto.NewUserDTO;
import johnoliveira.eventTracker_capstoneProject.dto.UserDTO;
import johnoliveira.eventTracker_capstoneProject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
     * URL_base+/users
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public UserDTO createUser(@RequestBody NewUserDTO newUserDTO) {
        return userService.createUser(newUserDTO);
    }

    /**
     * Lista di tutti gli utenti
     * richiesta GET:
     * URL_base+/users?page=0&size=10
     */
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.getAllUser(pageable));
    }

    /**
     * recupera un utente tramite il suo ID
     * richiesta GET:
     * URL_base+/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * elimina un utente tramite il suo ID
     * richiesta DELETE:
     * URL_base+/users/{id}
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }
}



