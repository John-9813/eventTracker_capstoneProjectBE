package johnoliveira.eventTracker_capstoneProject.controllers;

import johnoliveira.eventTracker_capstoneProject.dto.AuthRegisterDTO;
import johnoliveira.eventTracker_capstoneProject.dto.AuthRequestDTO;
import johnoliveira.eventTracker_capstoneProject.dto.AuthResponseDTO;
import johnoliveira.eventTracker_capstoneProject.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Esegui il login
     * richiesta POST:
     * URL_base+/login
     * body di esempio:
     * {
     *   "email": "esempio@example.com",
     *   "password": "Password123"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        try {
            System.out.println("Tentativo di login per l'utente: " + request.email());
            String token = authService.login(request.email(), request.password());
            System.out.println("Token generato: " + token);
            return ResponseEntity.ok(new AuthResponseDTO(token));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Errore durante il login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Esegui la registrazione
     * richiesta POST:
     * URL_base+/register
     * body di esempio:
     * {
     *   "name": "Nome"
     *   "surname": "Cognome"
     *   "email": "esempio@example.com",
     *   "password": "Password123!"
     * }
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> register(@RequestBody AuthRegisterDTO request) {
        try {
            System.out.println("Richiesta di registrazione ricevuta per email: " + request.email());
            authService.register(request);
            return ResponseEntity.ok("Registrazione completata con successo!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la registrazione: " + e.getMessage());
        }
    }

}

