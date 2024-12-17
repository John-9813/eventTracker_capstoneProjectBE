package johnoliveira.eventTracker_capstoneProject.dto;

import java.util.UUID;

public record SavedNewsCreateDTO(
        UUID userId,
        UUID newsId
) {}

