package johnoliveira.eventTracker_capstoneProject.dto;

import java.time.LocalDate;
import java.util.UUID;

public record SavedEventDTO(
        UUID savedEventId,
        UUID userId,
        UUID eventId,
        String notes,
        LocalDate savedAt
) {}

