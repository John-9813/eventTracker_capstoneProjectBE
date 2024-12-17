package johnoliveira.eventTracker_capstoneProject.services;



import johnoliveira.eventTracker_capstoneProject.dto.NewUserDTO;
import johnoliveira.eventTracker_capstoneProject.dto.UserDTO;
import johnoliveira.eventTracker_capstoneProject.entities.User;
import johnoliveira.eventTracker_capstoneProject.exceptions.NotFoundException;
import johnoliveira.eventTracker_capstoneProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    // crea un nuovo utente
    public UserDTO createUser(NewUserDTO newUserDTO) {
        String encodedPassword = passwordEncoder.encode(newUserDTO.password());

        User user = new User(
                newUserDTO.name(),
                newUserDTO.surname(),
                encodedPassword,
                newUserDTO.email()
        );
        return toUserDTO(userRepository.save(user));
    }

    // trova tutti gli utenti
    public Page<UserDTO> getAllUser(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toUserDTO);
    }

    // recupera utente tramite id
    public UserDTO getUserById(UUID userId) {
        return userRepository.findById(userId).map(this::toUserDTO).orElseThrow(() ->
                new NotFoundException("User not found with ID: " + userId));
    }

    // elimina utente tramite id
    public void deleteUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }

    // mapping del DTO
    private UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getName(),
                user.getSurname(),
                user.getEmail()
        );
    }
}




