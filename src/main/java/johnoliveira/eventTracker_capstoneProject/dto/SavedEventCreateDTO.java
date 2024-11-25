package johnoliveira.eventTracker_capstoneProject.dto;

import java.util.UUID;

public record SavedEventCreateDTO(
        UUID userId,
        UUID eventId,
        String notes
) {}

