package johnoliveira.eventTracker_capstoneProject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import johnoliveira.eventTracker_capstoneProject.entities.Event;


import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByCategory(String category);
}