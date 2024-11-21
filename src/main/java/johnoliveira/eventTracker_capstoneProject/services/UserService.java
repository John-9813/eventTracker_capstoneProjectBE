package johnoliveira.eventTracker_capstoneProject.services;


import johnoliveira.eventTracker_capstoneProject.entities.User;
import johnoliveira.eventTracker_capstoneProject.exceptions.NotFoundException;
import johnoliveira.eventTracker_capstoneProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // metodo per la creazione di un nuovo utente
    public User createUser(String name, String surname, String password, String email) {
        User user = new User(name, surname, password, email);
        return userRepository.save(user);
    }

    // metodo per ricercare un singolo utente con id
    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                        new NotFoundException("User not found with ID: " + userId));
    }

    // metodo per ricercare un singolo con email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                        new NotFoundException("User not found with email: " + email));
    }

    // metodo per permettere l'eliminazione dell'utente
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                        new NotFoundException("User not found with ID: " + userId));
        userRepository.delete(user);
    }

}

