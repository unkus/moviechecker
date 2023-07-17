package moviechecker.database;

import moviechecker.database.episode.EpisodeEntity;
import moviechecker.database.episode.EpisodeRepository;
import moviechecker.database.favorite.FavoriteEntity;
import moviechecker.database.movie.MovieEntity;
import moviechecker.database.movie.MovieRepository;
import moviechecker.database.season.SeasonEntity;
import moviechecker.database.season.SeasonRepository;
import moviechecker.database.site.SiteEntity;
import moviechecker.di.CheckerDatabase;
import moviechecker.database.favorite.FavoriteRepository;
import moviechecker.database.site.SiteRepository;
import moviechecker.di.Episode;
import moviechecker.di.Favorite;
import moviechecker.di.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class checkerDatabaseImpl implements CheckerDatabase {

    private @Autowired EpisodeRepository episodeRepository;
    private @Autowired SeasonRepository seasonRepository;
    private @Autowired MovieRepository movieRepository;
    private @Autowired SiteRepository siteRepository;
    private @Autowired FavoriteRepository favoriteRepository;

    @Override
    @Transactional
    public void cleanup() {
        siteRepository.findAll().forEach(site -> {
            site.getMovies().removeIf(movie -> !favoriteRepository.existsByMovie(movie));
            if (site.getMovies().isEmpty()) {
                siteRepository.delete(site);
            } else {
                siteRepository.save(site);
            }
        });
    }

    @Override
    public void deleteFavorite(Favorite favorite) {
        Optional<FavoriteEntity> entityOpt = favoriteRepository.findByMovie((MovieEntity) favorite.getMovie());
        entityOpt.ifPresent(favoriteRepository::delete);
    }

    @Override
    public void removeFromFavorite(Episode episode) {
        Optional<FavoriteEntity> entityOpt = favoriteRepository.findByMovie((MovieEntity) episode.getSeason().getMovie());
        entityOpt.ifPresent(favoriteRepository::delete);
    }

    @Override
    public void addToFavorites(Episode episode) {
        FavoriteEntity entity = new FavoriteEntity((MovieEntity) episode.getSeason().getMovie());
        favoriteRepository.save(entity);
    }

    @Override
    public boolean checkFavorites(Episode episode) {
        Optional<FavoriteEntity> entityOpt = favoriteRepository.findByMovie((MovieEntity) episode.getSeason().getMovie());
        return entityOpt.isPresent();
    }

    @Override
    public void markEpisodeViewed(Episode episode) {
        Optional<SiteEntity> siteOpt = siteRepository.findByAddress(episode.getSeason().getMovie().getSite().getAddress());
        siteOpt.ifPresent(siteEntity -> {
            Optional<MovieEntity> movieOpt = movieRepository.findBySiteAndPageId(siteEntity, episode.getSeason().getMovie().getPageId());
            movieOpt.ifPresent(movieEntity -> {
                Optional<SeasonEntity> seasonOpt = seasonRepository.findByMovieAndNumber(movieEntity, episode.getSeason().getNumber());
                seasonOpt.ifPresent(seasonEntity -> {
                    Optional<EpisodeEntity> episodeOpt = episodeRepository.findBySeasonAndNumber(seasonEntity, episode.getNumber());
                    episodeOpt.ifPresent(episodeEntity -> {
                        episodeEntity.setState(State.VIEWED);
                        episodeRepository.save(episodeEntity);
                    });
                });
            });
        });
    }
}
