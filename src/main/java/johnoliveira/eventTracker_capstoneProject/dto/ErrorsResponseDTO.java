package johnoliveira.eventTracker_capstoneProject.dto;

import java.time.LocalDateTime;

public record ErrorsResponseDTO(String message, LocalDateTime timestamp) {
}
