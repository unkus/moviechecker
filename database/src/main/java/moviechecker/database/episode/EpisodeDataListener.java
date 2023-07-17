package moviechecker.database.episode;

import jakarta.persistence.PostUpdate;
import moviechecker.di.events.EpisodeViewedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EpisodeDataListener {

    private @Autowired ApplicationEventPublisher applicationEventPublisher;

    @PostUpdate
    void postUpdate(EpisodeEntity episode) {
        applicationEventPublisher.publishEvent(new EpisodeViewedEvent(episode));
    }

}
