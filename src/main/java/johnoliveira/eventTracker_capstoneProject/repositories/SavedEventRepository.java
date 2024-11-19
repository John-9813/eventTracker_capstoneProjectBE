package johnoliveira.eventTracker_capstoneProject.repositories;


import johnoliveira.eventTracker_capstoneProject.entities.SavedEvent;
import johnoliveira.eventTracker_capstoneProject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SavedEventRepository extends JpaRepository<SavedEvent, UUID> {
    List<SavedEvent> findByUser(User user);
}

