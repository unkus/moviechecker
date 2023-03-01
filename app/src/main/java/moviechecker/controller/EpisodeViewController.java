package moviechecker.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import moviechecker.Tools;
import moviechecker.event.FavoriteRemovedEvent;
import moviechecker.event.FavoriteAddedEvent;
import moviechecker.model.Episode;
import moviechecker.model.FavoriteMovie;
import moviechecker.model.Movie;
import moviechecker.model.State;
import moviechecker.repository.EpisodeRepository;
import moviechecker.repository.FavoriteRepository;

@Component
public class EpisodeViewController {

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
		FavoriteMovie favorite = new FavoriteMovie(movie);
		favorites.save(favorite);
		applicationEventPublisher.publishEvent(new FavoriteAddedEvent(favorite));
	}

	public void removeFromFavorites(Movie movie) {
		Optional<FavoriteMovie> favoriteOpt = favorites.findByMovie(movie);
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
	}

	public void openInBrowser(Episode episode) {
		Optional<FavoriteMovie> favoriteOpt = favorites.findByMovie(episode.getMovie());
		favoriteOpt.ifPresent(favorite -> {
			favorite.setLastViewed(episode);
			favorites.save(favorite);
		});

		tools.openInBrowser(episode.getMovie().getSite().getLink().resolve(episode.getLink()));
	}

}
