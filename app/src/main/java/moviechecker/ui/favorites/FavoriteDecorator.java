package moviechecker.ui.favorites;

import moviechecker.di.Episode;
import moviechecker.di.Favorite;
import moviechecker.di.Movie;

import java.util.Optional;

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
