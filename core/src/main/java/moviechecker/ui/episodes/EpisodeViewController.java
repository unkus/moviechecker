package moviechecker.ui.episodes;

import moviechecker.core.di.CheckerDatabase;
import moviechecker.core.di.Episode;
import moviechecker.core.di.events.EpisodeViewedEvent;
import moviechecker.ui.ItemController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EpisodeViewController extends ItemController {

    private @Autowired CheckerDatabase database;

    private Map<Episode, EpisodeView> viewMap = new HashMap<>();

    public EpisodeView getView(Episode episode) {
        EpisodeView view = viewMap.get(episode);
        if (view == null) {
            switch (episode.getState()) {
                case RELEASED, VIEWED -> view = new ReleasedEpisodeView(this);
                default -> view = new EpisodeView(this);
            }
            view.bind(episode);
            viewMap.put(episode, view);
        }
        return view;
    }

    public void onClick$AddToFavorites(Episode episode) {
        database.addToFavorites(episode);
    }

    public void onClick$RemoveFromFavorites(Episode episode) {
        database.removeFromFavorite(episode);
    }

    public boolean isInFavorites(Episode episode) {
        return database.checkFavorites(episode);
    }

    public void markViewed(Episode episode) {
        database.markEpisodeViewed(episode);
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

    @EventListener
    public void handleEpisodeViewed(EpisodeViewedEvent event) {
        Episode episode = (Episode) event.getSource();
        EpisodeView view = viewMap.get(episode);
        view.bind(episode);
        view.getParent().validate();
    }
}
