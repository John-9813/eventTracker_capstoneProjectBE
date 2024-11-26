package johnoliveira.eventTracker_capstoneProject.repositories;

import johnoliveira.eventTracker_capstoneProject.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import johnoliveira.eventTracker_capstoneProject.entities.Event;


import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    // cerca eventi per categoria con paginazione
    Page<Event> findByCategory(Category category, Pageable pageable);

    // cerca eventi per parola chiave con paginazione
    Page<Event> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String titleKeyword, String descriptionKeyword, Pageable pageable);
}
