package johnoliveira.eventTracker_capstoneProject.repositories;

import johnoliveira.eventTracker_capstoneProject.entities.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface NewsRepository extends JpaRepository<News, UUID> {
    // Cerca notizie per parola chiave con paginazione
    Page<News> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String titleKeyword, String descriptionKeyword, Pageable pageable);
}

