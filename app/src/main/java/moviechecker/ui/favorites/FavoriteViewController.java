package moviechecker.ui.favorites;

import moviechecker.ui.ItemController;
import moviechecker.ui.Tools;
import moviechecker.ui.events.FavoriteRemovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import moviechecker.database.favorite.Favorite;
import moviechecker.database.favorite.FavoriteRepository;

@Component
public class FavoriteViewController extends ItemController {

	private Logger logger = LoggerFactory.getLogger(FavoriteViewController.class);

	@Autowired
    private ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	private FavoriteRepository favorites;

	public void removeFromFavorites(Favorite favorite) {
		favorites.delete(favorite);
		applicationEventPublisher.publishEvent(new FavoriteRemovedEvent(favorite));
	}

}
