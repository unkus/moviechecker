package moviechecker.database;

import moviechecker.database.episode.EpisodeEntity;
import moviechecker.database.episode.EpisodeH2CrudRepository;
import moviechecker.database.movie.MovieEntity;
import moviechecker.database.movie.MovieRepository;
import moviechecker.database.season.SeasonEntity;
import moviechecker.database.season.SeasonRepository;
import moviechecker.database.site.SiteEntity;
import moviechecker.database.site.SiteRepository;
import moviechecker.core.di.State;
import moviechecker.datasource.di.DataRecord;
import moviechecker.datasource.di.DataRecordPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataRecordPublisherImpl implements DataRecordPublisher {

    private @Autowired SiteRepository sites;
    private @Autowired MovieRepository movies;
    private @Autowired SeasonRepository seasons;
    private @Autowired EpisodeH2CrudRepository episodes;

    @Override
    public void publishRecord(DataRecord record) {
        SiteEntity site = sites.findByAddress(record.siteAddress()).orElse(new SiteEntity(record.siteAddress()));
        sites.save(site);

        MovieEntity movie = movies.findBySiteAndPageId(site, record.moviePageId()).orElse(new MovieEntity(site, record.moviePageId()));
        movie.setTitle(record.movieTitle());
        movie.setPath(record.moviePath());
        movie.setPosterLink(record.moviePosterLink());
        movies.save(movie);

        SeasonEntity season = seasons.findByMovieAndNumber(movie, record.seasonNumber()).orElse(new SeasonEntity(movie, record.seasonNumber()));
        season.setPath(record.seasonPath());
        seasons.save(season);

        EpisodeEntity episode = episodes.findBySeasonAndNumber(season, record.episodeNumber()).orElse(new EpisodeEntity(season, record.episodeNumber()));
        episode.setTitle(record.episodeTitle());
        episode.setPath(record.episodePath());
        if (episode.getState() != State.VIEWED) {
            episode.setState(record.episodeState());
        }
        episode.setDate(record.episodeDate());
        episodes.save(episode);
    }
}
