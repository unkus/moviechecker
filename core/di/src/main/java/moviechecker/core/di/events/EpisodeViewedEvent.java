package moviechecker.core.di.events;

import moviechecker.core.di.Episode;
import org.springframework.context.ApplicationEvent;

public class EpisodeViewedEvent extends ApplicationEvent {
    public EpisodeViewedEvent(Episode source) {
        super(source);
    }
}
