package moviechecker.ui.favorites;

import moviechecker.di.CheckerDatabase;
import moviechecker.di.Favorite;
import moviechecker.ui.ItemController;
import moviechecker.ui.events.FavoriteRemovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class FavoriteViewController extends ItemController {

	private Logger logger = LoggerFactory.getLogger(FavoriteViewController.class);

    private @Autowired ApplicationEventPublisher applicationEventPublisher;
	
	private @Autowired CheckerDatabase database;

	public void removeFromFavorites(Favorite favorite) {
		database.deleteFavorite(favorite);

		applicationEventPublisher.publishEvent(new FavoriteRemovedEvent(favorite));
	}

}
