package johnoliveira.eventTracker_capstoneProject.services;



import johnoliveira.eventTracker_capstoneProject.dto.NewUserDTO;
import johnoliveira.eventTracker_capstoneProject.dto.UserDTO;
import johnoliveira.eventTracker_capstoneProject.entities.User;
import johnoliveira.eventTracker_capstoneProject.exceptions.NotFoundException;
import johnoliveira.eventTracker_capstoneProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    // crea un nuovo utente
    public UserDTO createUser(NewUserDTO newUserDTO) {
        User user = new User(
                newUserDTO.name(),
                newUserDTO.surname(),
                newUserDTO.password(),
                newUserDTO.email()
        );
        return toUserDTO(userRepository.save(user));
    }

    // recupera utente tramite id
    public UserDTO getUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(this::toUserDTO)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
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




