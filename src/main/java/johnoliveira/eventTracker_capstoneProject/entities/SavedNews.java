package johnoliveira.eventTracker_capstoneProject.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "saved_news")
public class SavedNews {

    @Id
    @GeneratedValue
    @Column(name = "saved_news_id", nullable = false, updatable = false)
    private UUID savedNewsId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

    @Column(name = "saved_at", nullable = false)
    private LocalDate savedAt = LocalDate.now();
}

