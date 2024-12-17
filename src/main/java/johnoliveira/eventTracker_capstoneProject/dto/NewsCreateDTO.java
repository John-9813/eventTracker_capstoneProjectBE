package johnoliveira.eventTracker_capstoneProject.dto;

import java.time.LocalDate;

public record NewsCreateDTO(
        String title,
        String description,
        String imageUrl,
        LocalDate publishedDate,
        String source,
        String url
) {}

