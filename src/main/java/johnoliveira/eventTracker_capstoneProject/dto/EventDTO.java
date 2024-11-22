package johnoliveira.eventTracker_capstoneProject.dto;

import java.time.LocalDate;
import java.util.UUID;

public record EventDTO(
        UUID eventId,
        String title,
        String description,
        String imageUrl,
        LocalDate startDate,
        LocalDate endDate,
        String location,
        String pageUrl,
        String category
) {}

