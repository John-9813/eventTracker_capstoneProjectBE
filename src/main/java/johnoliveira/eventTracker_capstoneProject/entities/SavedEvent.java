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
@Table(name = "saved_events")
public class SavedEvent {

    @Id
    @GeneratedValue
    @Column(name = "saved_event_id", nullable = false, updatable = false)
    private UUID savedEventId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "saved_at", nullable = false)
    private LocalDate savedAt = LocalDate.now();

    @Column(name = "notes", length = 1000)
    private String notes;
}

