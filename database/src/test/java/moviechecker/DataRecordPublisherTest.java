package moviechecker;

import moviechecker.core.di.State;
import moviechecker.database.episode.EpisodeEntity;
import moviechecker.database.episode.EpisodeH2CrudRepository;
import moviechecker.database.movie.MovieEntity;
import moviechecker.database.movie.MovieRepository;
import moviechecker.database.season.SeasonEntity;
import moviechecker.database.season.SeasonRepository;
import moviechecker.database.site.SiteEntity;
import moviechecker.database.site.SiteRepository;
import moviechecker.datasource.di.DataRecord;
import moviechecker.datasource.di.DataRecordPublisher;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@SpringBootTest
public class DataRecordPublisherTest {
    @Autowired
    private DataRecordPublisher dataRecordPublisher;

    @Autowired
    private SiteRepository sites;
    @Autowired
    private MovieRepository movies;
    @Autowired
    private SeasonRepository seasons;
    @Autowired
    private EpisodeH2CrudRepository episodes;

    @Test
    public void publishRecordTest() throws Exception {
        assertNotNull(dataRecordPublisher);
        assertEquals(0, sites.count());

        DataRecord.Builder recordBuilder = new DataRecord.Builder();
        recordBuilder.site("https://test.test")
                .moviePageId("movie")
                .movieTitle("movie")
                .moviePath("/movie")
                .moviePosterLink("/poster_one.jpg")
                .seasonNumber(1)
                .seasonPath("/movie/season")
                .episodeNumber(1)
                .episodeTitle("episode 1")
                .episodePath("/movie/season/episode")
                .episodeState(State.EXPECTED)
                .episodeDate(LocalDateTime.now());

        dataRecordPublisher.publishRecord(recordBuilder.build());

        // Check whether the data were stored.
        Optional<SiteEntity> siteOpt = sites.findByAddress(URI.create("https://test.test"));
        assertTrue(siteOpt.isPresent());
        siteOpt.ifPresent(site -> {
            assertEquals("https://test.test", site.getAddress().toString());
        });
        Optional<MovieEntity> movieOpt = movies.findBySiteAndPageId(siteOpt.get(), "movie");
        assertTrue(movieOpt.isPresent());
        movieOpt.ifPresent(movie -> {
            assertEquals("movie", movie.getPageId());
            assertEquals("/movie", movie.getPath());
            assertEquals("movie", movie.getTitle());
            assertEquals("/poster_one.jpg", movie.getPosterLink().toString());
        });
        Optional<SeasonEntity> seasonOpt = seasons.findByMovieAndNumber(movieOpt.get(), 1);
        assertTrue(seasonOpt.isPresent());
        seasonOpt.ifPresent(season -> {
            assertEquals("/movie/season", season.getPath());
            assertEquals(1, season.getNumber());
        });
        Optional<EpisodeEntity> episodeOpt = episodes.findBySeasonAndNumber(seasonOpt.get(), 1);
        assertTrue(episodeOpt.isPresent());
        episodeOpt.ifPresent(episode -> {
            assertEquals("/movie/season/episode", episode.getPath());
            assertEquals("episode 1", episode.getTitle());
            assertEquals(1, episode.getNumber());
            assertTrue(LocalDateTime.of(LocalDate.now(), LocalTime.MIN).isBefore(episode.getReleaseDate()));
            assertEquals(State.EXPECTED, episode.getState());
        });
    }
}
