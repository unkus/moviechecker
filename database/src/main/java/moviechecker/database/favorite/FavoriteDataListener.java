package moviechecker.database.favorite;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import moviechecker.core.di.events.FavoriteAddedEvent;
import moviechecker.core.di.events.FavoriteRemovedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class FavoriteDataListener {

    private @Autowired ApplicationEventPublisher applicationEventPublisher;

    @PostPersist
    void postPersist(FavoriteEntity favorite) {
        applicationEventPublisher.publishEvent(new FavoriteAddedEvent(favorite));
    }

    @PostRemove
    void postRemove(FavoriteEntity favorite) {
        applicationEventPublisher.publishEvent(new FavoriteRemovedEvent(favorite));
    }
}
