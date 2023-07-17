package moviechecker.ui.favorites;

import moviechecker.di.Favorite;
import moviechecker.ui.Linkable;

import java.net.URI;

public class LinkableFavorite extends FavoriteDecorator implements Linkable {
    LinkableFavorite(Favorite favorite) {
        super(favorite);
    }

    @Override
    public URI getLink() {
        return favorite.getMovie().getSite().getAddress().resolve(favorite.getMovie().getPath());
    }
}
