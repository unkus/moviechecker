package moviechecker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import moviechecker.Tools;
import moviechecker.event.FavoriteRemovedEvent;
import moviechecker.model.FavoriteMovie;
import moviechecker.repository.FavoriteRepository;
import moviechecker.view.ExpectedEpisodeView;

@Component
public class FavoriteViewController {

	private Logger logger = LoggerFactory.getLogger(ExpectedEpisodeView.class);

	@Autowired
    private ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	private Tools tools;
	
	@Autowired
	private FavoriteRepository favorites;

	public void removeFromFavorites(FavoriteMovie favorite) {
		favorites.delete(favorite);
		applicationEventPublisher.publishEvent(new FavoriteRemovedEvent(favorite));
	}

	public void openInBrowser(FavoriteMovie favorite) {
		tools.openInBrowser(favorite.getMovie().getSite().getLink().resolve(favorite.getMovie().getLink()));
	}

}
