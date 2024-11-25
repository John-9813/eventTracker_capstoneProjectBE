package johnoliveira.eventTracker_capstoneProject.dto;

import java.time.LocalDate;

public record EventCreateDTO(
        String title,
        String description,
        String imageUrl,
        LocalDate startDate,
        LocalDate endDate,
        String location,
        String pageUrl,
        String category
) {}
