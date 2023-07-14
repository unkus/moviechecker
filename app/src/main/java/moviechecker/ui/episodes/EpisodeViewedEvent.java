package moviechecker.ui.episodes;

import moviechecker.di.Episode;
import org.springframework.context.ApplicationEvent;

public class EpisodeViewedEvent extends ApplicationEvent {
    public EpisodeViewedEvent(Episode source) {
        super(source);
    }


}
