package johnoliveira.eventTracker_capstoneProject.dto;

import java.time.LocalDate;

public record EventCreateDTO(
        String title,
        String imageUrl,
        LocalDate startDate,
        String startTime,
        LocalDate endDate,
        String location,
        String pageUrl,
        String ticketStatus,
        String city
) {}
