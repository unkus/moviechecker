package moviechecker;

import moviechecker.event.DataReceivedEvent;
import moviechecker.event.DataRequestedEvent;
import moviechecker.model.Episode;
import moviechecker.model.Movie;
import moviechecker.model.Season;
import moviechecker.model.Site;
import moviechecker.provider.MovieProvider;
import moviechecker.repository.EpisodeRepository;
import moviechecker.repository.MovieRepository;
import moviechecker.repository.SeasonRepository;
import moviechecker.repository.SiteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.net.URI;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RecordApplicationEvents
public class ProviderTest {

    @Autowired private ApplicationEventPublisher applicationEventPublisher;
    @SpyBean private MovieProvider provider;
    @SpyBean private TestDataReceiver component;

    @Autowired private SiteRepository sites;
    @Autowired private MovieRepository movies;
    @Autowired private SeasonRepository seasons;
    @Autowired private EpisodeRepository episodes;

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
