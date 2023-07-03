package moviechecker.provider;

import moviechecker.model.*;
import moviechecker.repository.EpisodeRepository;
import moviechecker.repository.MovieRepository;
import moviechecker.repository.SeasonRepository;
import moviechecker.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import moviechecker.event.DataErrorEvent;
import moviechecker.event.DataReceivedEvent;
import moviechecker.event.DataRequestedEvent;
import org.springframework.context.event.EventListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractMovieProvider implements MovieProvider {

    private @Autowired ApplicationEventPublisher applicationEventPublisher;

    private @Autowired SiteRepository sites;
    private @Autowired MovieRepository movies;
    private @Autowired SeasonRepository seasons;
    private @Autowired EpisodeRepository episodes;

    private final ExecutorService dataRetrievalExecutor = Executors.newSingleThreadExecutor();
    private final ExecutorService recordSavingExecutor = Executors.newSingleThreadExecutor();

    @EventListener
    public final void handleDataRequest(DataRequestedEvent event) {
        dataRetrievalExecutor.submit(()-> {
            try {
                retrieveData();
                applicationEventPublisher.publishEvent(new DataReceivedEvent(this));
            } catch (Exception e) {
                applicationEventPublisher.publishEvent(new DataErrorEvent(e));
            }
        });
    }

    /**
     * Apply the data from the record to the database.
     *
     * @param record - data to be saved in the database
     */
    protected final void saveRecord(DataRecord record) {
        Site site = sites.findByAddress(record.siteAddress()).orElse(new Site(record.siteAddress()));
        sites.save(site);

        Movie movie = movies.findBySiteAndPageId(site, record.moviePageId()).orElse(new Movie(site, record.moviePageId()));
        movie.setTitle(record.movieTitle());
        movie.setLink(record.movieLink());
        movies.save(movie);

        Season season = seasons.findByMovieAndNumber(movie, record.seasonNumber()).orElse(new Season(movie, record.seasonNumber()));
        seasons.save(season);

        Episode episode = episodes.findBySeasonAndNumber(season, record.episodeNumber()).orElse(new Episode(season, record.episodeNumber()));
        episode.setTitle(record.episodeTitle());
        episode.setLink(record.episodeLink());
        if (episode.getState() != State.VIEWED) {
            episode.setState(record.episodeState());
        }
        episode.setDate(record.episodeDate());
        episodes.save(episode);
    }
}
