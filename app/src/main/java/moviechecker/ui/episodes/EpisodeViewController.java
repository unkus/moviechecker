package moviechecker.ui.episodes;

import java.util.Optional;

import moviechecker.ui.ItemController;
import moviechecker.ui.Tools;
import moviechecker.ui.events.FavoriteAddedEvent;
import moviechecker.ui.events.FavoriteRemovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import moviechecker.database.episode.Episode;
import moviechecker.database.favorite.Favorite;
import moviechecker.database.movie.Movie;
import moviechecker.database.State;
import moviechecker.database.episode.EpisodeRepository;
import moviechecker.database.favorite.FavoriteRepository;

@Component
public class EpisodeViewController extends ItemController {

    private Logger logger = LoggerFactory.getLogger(EpisodeViewController.class);

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private Tools tools;

    @Autowired
    private FavoriteRepository favorites;

    @Autowired
    private EpisodeRepository episodes;

    public void addToFavorites(Movie movie) {
        Favorite favorite = new Favorite(movie);
        favorites.save(favorite);
        applicationEventPublisher.publishEvent(new FavoriteAddedEvent(favorite));
    }

    public void removeFromFavorites(Movie movie) {
        Optional<Favorite> favoriteOpt = favorites.findByMovie(movie);
        favoriteOpt.ifPresent(favorite -> {
            favorites.delete(favorite);
            applicationEventPublisher.publishEvent(new FavoriteRemovedEvent(favoriteOpt.get()));
        });
    }

    public boolean isInFavorites(Episode episode) {
        return favorites.existsByMovie(episode.getMovie());
    }

    public void markViewed(Episode episode) {
        episode.setState(State.VIEWED);
        episodes.save(episode);

        Optional<Favorite> favoriteOpt = favorites.findByMovie(episode.getMovie());
        favoriteOpt.ifPresent(favorite -> {
            favorite.setLastViewed(episode);
            favorites.save(favorite);
        });
    }

}
