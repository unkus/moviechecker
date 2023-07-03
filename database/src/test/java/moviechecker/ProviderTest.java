package moviechecker;

import moviechecker.database.episode.Episode;
import moviechecker.database.episode.EpisodeRepository;
import moviechecker.database.movie.Movie;
import moviechecker.database.movie.MovieRepository;
import moviechecker.database.season.Season;
import moviechecker.database.season.SeasonRepository;
import moviechecker.database.site.Site;
import moviechecker.database.site.SiteRepository;
import moviechecker.datasource.events.DataReceivedEvent;
import moviechecker.datasource.events.DataRequestedEvent;
import moviechecker.datasource.provider.MovieProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.net.URI;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@RecordApplicationEvents
public class ProviderTest {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @SpyBean
    private MovieProvider provider;
    @SpyBean
    private TestDataReceiver component;

    @Autowired
    private SiteRepository sites;
    @Autowired
    private MovieRepository movies;
    @Autowired
    private SeasonRepository seasons;
    @Autowired
    private EpisodeRepository episodes;

    @Test
    public void dataRequestProcessed() throws Exception {
        Assertions.assertEquals(0, sites.count());

        // Send indication to TestProvider that the data should be read.
        applicationEventPublisher.publishEvent(new DataRequestedEvent(this));

        // TODO: How can I verify that the data is being retrieved in another thread?

        // Checks whether the method contains data retrieval logic is called.
        verify(provider, timeout(1000).times(1)).retrieveData();

        // Checks whether the provider send back the DataReceivedEvent after data provided.
        verify(component, timeout(1000).times(1)).handleDataReceived(any(DataReceivedEvent.class));

        // Check whether the data were stored.
        Optional<Site> siteOpt = sites.findByAddress(URI.create("https://test.test"));
        Assertions.assertTrue(siteOpt.isPresent());
        Optional<Movie> movieOpt = movies.findBySiteAndPageId(siteOpt.get(), "movie");
        Assertions.assertTrue(movieOpt.isPresent());
        Optional<Season> seasonOpt = seasons.findByMovieAndNumber(movieOpt.get(), 1);
        Assertions.assertTrue(seasonOpt.isPresent());
        Optional<Episode> episodeOpt = episodes.findBySeasonAndNumber(seasonOpt.get(), 1);
        Assertions.assertTrue(episodeOpt.isPresent());
    }

}
