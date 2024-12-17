package johnoliveira.eventTracker_capstoneProject.dto;

import java.time.LocalDate;
import java.util.UUID;

public record SavedNewsDTO(
        UUID savedNewsId,
        UUID userId,
        UUID newsId,
        LocalDate savedAt
) {}

