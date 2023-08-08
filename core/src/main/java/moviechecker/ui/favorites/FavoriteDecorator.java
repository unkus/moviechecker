package moviechecker.ui.favorites;

import moviechecker.core.di.Favorite;
import moviechecker.core.di.Movie;

public abstract class FavoriteDecorator implements Favorite {

    final Favorite favorite;

    FavoriteDecorator(Favorite favorite) {
        this.favorite = favorite;
    }

    @Override
    public final Movie getMovie() {
        return favorite.getMovie();
    }

    @Override
    public final String getTitle() {
        return favorite.getTitle();
    }
}
