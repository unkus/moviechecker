package moviechecker.ui.favorites;

import moviechecker.di.CheckerDatabase;
import moviechecker.di.Favorite;
import moviechecker.ui.ItemController;
import moviechecker.di.events.FavoriteRemovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class FavoriteViewController extends ItemController {

	private @Autowired CheckerDatabase database;

	public void removeFromFavorites(Favorite favorite) {
		database.deleteFavorite(favorite);
	}

}
