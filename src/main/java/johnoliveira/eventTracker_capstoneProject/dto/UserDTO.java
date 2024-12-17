package johnoliveira.eventTracker_capstoneProject.dto;

import java.util.UUID;

public record UserDTO(
        UUID userId,
        String name,
        String surname,
        String email
) {}

