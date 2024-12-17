package johnoliveira.eventTracker_capstoneProject.dto;


import java.time.LocalDate;

public record EventDTO(
        String eventId,
        String title,
        String imageUrl,
        LocalDate startDate,
        String startTime,
        String venueName,
        String pageUrl,
        String ticketStatus,
        String city
) {}







