package johnoliveira.eventTracker_capstoneProject.dto;

import java.time.LocalDate;
import java.util.UUID;

public record NewsDTO(
        UUID newsId,
        String title,
        String description,
        String imageUrl,
        LocalDate publishedDate,
        String source,
        String url
) {}

