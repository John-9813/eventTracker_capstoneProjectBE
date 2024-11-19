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
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue
    @Column(name = "news_id", nullable = false, updatable = false)
    private UUID newsId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "published_date", nullable = false)
    private LocalDate publishedDate;

    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "url", nullable = false)
    private String url;
}

