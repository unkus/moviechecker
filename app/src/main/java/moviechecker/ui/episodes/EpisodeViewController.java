package moviechecker.ui.episodes;

import moviechecker.di.CheckerDatabase;
import moviechecker.di.Episode;
import moviechecker.ui.ItemController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import moviechecker.database.episode.EpisodeRepository;

@Component
public class EpisodeViewController extends ItemController {

    private Logger logger = LoggerFactory.getLogger(EpisodeViewController.class);

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private CheckerDatabase database;

    @Autowired
    private EpisodeRepository episodes;

    public void onClick$AddToFavorites(Episode episode) {
        database.addToFavorites(episode);
//        applicationEventPublisher.publishEvent(new FavoriteAddedEvent(favorite));
    }

    public void onClick$RemoveFromFavorites(Episode episode) {
        database.removeFromFavorite(episode);
//        applicationEventPublisher.publishEvent(new FavoriteRemovedEvent(favoriteOpt.get()));
    }

    public boolean isInFavorites(Episode episode) {
        return database.checkFavorites(episode);
    }

    public void markViewed(Episode episode) {
//        episode.setState(State.VIEWED);
//        episodes.save(episode);
//
//        Optional<Favorite> favoriteOpt = favorites.findByMovie(episode.getSeason().getMovie());
//        favoriteOpt.ifPresent(favorite -> {
//            Optional<Episode> lastViewedOpt = favorite.getLastViewed();
//            lastViewedOpt.ifPresent(lastViewed -> {
//                if (episode.getNumber() > lastViewed.getNumber()) {
//                    favorite.setLastViewed(episode);
//                    favorites.save(favorite);
//                }
//            });
//        });
//
//        // inform view about change
//        applicationEventPublisher.publishEvent(new EpisodeViewedEvent(episode));
    }

}
