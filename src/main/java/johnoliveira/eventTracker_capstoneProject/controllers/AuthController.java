package johnoliveira.eventTracker_capstoneProject.controllers;

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
            System.out.println("Email ricevuta: " + request.email());
            System.out.println("Password ricevuta: " + request.password());
            String token = authService.login(request.email(), request.password());
            return ResponseEntity.ok(new AuthResponseDTO(token));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

