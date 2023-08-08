package moviechecker.ui.favorites;

import moviechecker.core.di.CheckerDatabase;
import moviechecker.core.di.Favorite;
import moviechecker.ui.ItemController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FavoriteViewController extends ItemController {

	private @Autowired CheckerDatabase database;

	public void removeFromFavorites(Favorite favorite) {
		database.deleteFavorite(favorite);
	}

}
