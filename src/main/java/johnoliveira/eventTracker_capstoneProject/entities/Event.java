package johnoliveira.eventTracker_capstoneProject.entities;

import jakarta.persistence.*;

import johnoliveira.eventTracker_capstoneProject.enums.Category;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue
    @Column(name = "event_id", nullable = false, updatable = false)
    private UUID eventId;

    @Column(name = "api_id", unique = true)
    private String apiId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "page_url")
    private String pageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;
}


