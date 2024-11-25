package johnoliveira.eventTracker_capstoneProject.dto;

import java.util.UUID;

public record NewUserDTO(
        String name,
        String surname,
        String email,
        String password
)
{}
