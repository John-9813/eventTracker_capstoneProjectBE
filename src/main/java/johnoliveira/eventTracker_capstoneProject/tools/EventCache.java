package johnoliveira.eventTracker_capstoneProject.tools;

import johnoliveira.eventTracker_capstoneProject.dto.EventDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventCache {
    private List<EventDTO> cachedEvents = new ArrayList<>();
    private long lastUpdate = 0;

    public List<EventDTO> getCachedEvents() {
        if (System.currentTimeMillis() - lastUpdate < 15 * 60 * 1000) {
            System.out.println("Restituzione eventi dalla cache. Numero di eventi: " + cachedEvents.size());
            return cachedEvents;
        }
        System.out.println("Cache scaduta o vuota.");
        return null;
    }


    public void updateCache(List<EventDTO> events) {
        this.cachedEvents = events;
        this.lastUpdate = System.currentTimeMillis();
    }
}

