package moviechecker.database;

import moviechecker.database.favorite.FavoriteEntity;
import moviechecker.database.movie.MovieEntity;
import moviechecker.di.CheckerDatabase;
import moviechecker.database.favorite.FavoriteRepository;
import moviechecker.database.site.SiteRepository;
import moviechecker.di.Episode;
import moviechecker.di.Favorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class checkerDatabaseImpl implements CheckerDatabase {

    @Autowired
    private FavoriteRepository favorites;

    @Autowired
    private SiteRepository sites;

    @Override
    public void cleanup() {
        sites.findAll().forEach(site -> {
            site.getMovies().removeIf(movie -> !favorites.existsByMovie(movie));
            if (site.getMovies().isEmpty()) {
                sites.delete(site);
            } else {
                sites.save(site);
            }
        });
    }

    @Override
    public void deleteFavorite(Favorite favorite) {
        Optional<FavoriteEntity> entityOpt = favorites.findByMovie((MovieEntity) favorite.getMovie());
        entityOpt.ifPresent(favorites::delete);
    }

    @Override
    public void removeFromFavorite(Episode episode) {
        Optional<FavoriteEntity> entityOpt = favorites.findByMovie((MovieEntity) episode.getSeason().getMovie());
        entityOpt.ifPresent(favorites::delete);
    }

    @Override
    public void addToFavorites(Episode episode) {
        FavoriteEntity entity = new FavoriteEntity((MovieEntity) episode.getSeason().getMovie());
        favorites.save(entity);
    }

    @Override
    public boolean checkFavorites(Episode episode) {
        Optional<FavoriteEntity> entityOpt = favorites.findByMovie((MovieEntity) episode.getSeason().getMovie());
        return entityOpt.isPresent();
    }
}
