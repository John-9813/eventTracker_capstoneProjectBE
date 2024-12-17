package johnoliveira.eventTracker_capstoneProject.dto;

public record AuthRegisterDTO(
        String email,
        String password,
        String name,
        String surname
) {}

