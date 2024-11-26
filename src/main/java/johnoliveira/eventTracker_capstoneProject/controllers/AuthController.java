package johnoliveira.eventTracker_capstoneProject.controllers;

import johnoliveira.eventTracker_capstoneProject.dto.AuthRequestDTO;
import johnoliveira.eventTracker_capstoneProject.dto.AuthResponseDTO;
import johnoliveira.eventTracker_capstoneProject.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
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
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        String token = authService.login(request.email(), request.password());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}
