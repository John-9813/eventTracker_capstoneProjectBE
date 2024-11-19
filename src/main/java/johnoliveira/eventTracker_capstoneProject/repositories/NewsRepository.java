package johnoliveira.eventTracker_capstoneProject.repositories;

import johnoliveira.eventTracker_capstoneProject.entities.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NewsRepository extends JpaRepository<News, UUID> {
}

