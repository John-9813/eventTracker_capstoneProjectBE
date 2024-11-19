package johnoliveira.eventTracker_capstoneProject.repositories;

import johnoliveira.eventTracker_capstoneProject.entities.SavedNews;
import johnoliveira.eventTracker_capstoneProject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SavedNewsRepository extends JpaRepository<SavedNews, UUID> {
    List<SavedNews> findByUser(User user);
}
