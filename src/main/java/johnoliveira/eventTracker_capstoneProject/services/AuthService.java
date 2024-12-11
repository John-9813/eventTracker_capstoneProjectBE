package johnoliveira.eventTracker_capstoneProject.services;

import johnoliveira.eventTracker_capstoneProject.dto.AuthRegisterDTO;
import johnoliveira.eventTracker_capstoneProject.entities.User;
import johnoliveira.eventTracker_capstoneProject.exceptions.UnauthorizedException;
import johnoliveira.eventTracker_capstoneProject.repositories.UserRepository;
import johnoliveira.eventTracker_capstoneProject.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWT jwt;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        return jwt.generateToken(user.getUserId().toString());
    }

    public void register(AuthRegisterDTO request) {
        // Controlla se l'email è già registrata
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalStateException("Email già registrata!");
        }
        // Crea il nuovo utente
        User newUser = new User();
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setName(request.name());
        newUser.setSurname(request.surname());
        newUser.setCreatedAt(LocalDate.now());
        // Salva l'utente nel database
        userRepository.save(newUser);
    }
}


