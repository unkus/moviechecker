package moviechecker;

import java.net.URI;
import java.util.*;
import java.util.stream.StreamSupport;

import moviechecker.database.episode.EpisodeRepository;
import moviechecker.database.movie.MovieRepository;
import moviechecker.database.season.SeasonRepository;
import moviechecker.database.site.SiteRepository;
import moviechecker.database.favorite.FavoriteRepository;
import moviechecker.di.Episode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import moviechecker.database.episode.EpisodeEntity;
import moviechecker.database.favorite.FavoriteEntity;
import moviechecker.database.movie.MovieEntity;
import moviechecker.database.season.SeasonEntity;
import moviechecker.database.site.SiteEntity;
import moviechecker.di.State;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "/mainTestData.sql")
public class MainTest {
    @Autowired
    private SiteRepository sites;
    @Autowired
    private MovieRepository movies;
    @Autowired
    private SeasonRepository seasons;
    @Autowired
    private EpisodeRepository episodes;
    @Autowired
    private FavoriteRepository favorites;

    @AfterEach
    public void cleanupData() {
        favorites.deleteAll();
        sites.deleteAll();
    }

    @Test
    public void findAllByStateOrderByReleaseDateAscTest() {
        assertNotNull(episodes);

        Iterable<Episode> episodesIterable = episodes.findAllByStateOrderByDateAsc(State.EXPECTED);
        List<Episode> actualList = StreamSupport.stream(episodesIterable.spliterator(), false)
                .toList();

        List<Episode> sortedList = actualList.stream()
                .sorted(Comparator.comparing(item -> item.getReleaseDate(),
                        (left, right) -> left != null ? (right != null ? left.compareTo(right) : 1) : -1))
                .toList();

        assertEquals(sortedList, actualList, "The list of episodes is unsorted");
    }

    @Test
    public void findAllByStateOrderByReleaseDateDescTest() {
        assertNotNull(episodes);

        Iterable<Episode> episodesIterable = episodes.findAllByStateNotOrderByDateDesc(State.RELEASED);
        List<Episode> actualList = StreamSupport.stream(episodesIterable.spliterator(), false)
                .toList();

        List<Episode> sortedList = actualList.stream()
                .sorted(Comparator.comparing(item -> item.getReleaseDate(),
                        (left, right) -> (left != null ? (right != null ? left.compareTo(right) : 1) : -1) * -1))
                .toList();

        assertEquals(sortedList, actualList, "The list of episodes is unsorted");
    }

    @Test
    public void findByAddressTest() {
        assertNotNull(sites);

        URI address = URI.create("https://site.one");
        Optional<SiteEntity> siteOpt = sites.findByAddress(address);
        assertTrue(siteOpt.isPresent(), "Expected site not found");

        SiteEntity site = siteOpt.get();
        assertEquals(address, site.getAddress());

        assertTrue(sites.findByAddress(URI.create("https://site.three")).isEmpty(), "Unexpected site found");
    }

    @Test
    public void findBySiteAndPageTest() {
        assertNotNull(sites);
        assertNotNull(movies);

        Optional<SiteEntity> siteOpt = sites.findByAddress(URI.create("https://site.one"));
        assertTrue(siteOpt.isPresent(), "Expected site not found");
        SiteEntity site = siteOpt.get();

        Optional<MovieEntity> movieOpt = movies.findBySiteAndPageId(site, "movie_one");
        assertTrue(movieOpt.isPresent(), "Expected movie not found");

        MovieEntity movie = movieOpt.get();
        assertEquals(site, movie.getSite());
        assertEquals("movie_one", movie.getPageId(), "Unexpected movie found");

        assertTrue(movies.findBySiteAndPageId(site, "movie_four").isEmpty(), "Unexpected movie found");
    }

    @Test
    public void findByMovieAndNumberTest() {
        assertNotNull(sites);
        assertNotNull(movies);
        assertNotNull(seasons);
        assertNotNull(episodes);

        Optional<SiteEntity> siteOpt = sites.findByAddress(URI.create("https://site.one"));
        assertTrue(siteOpt.isPresent(), "Expected site not found");
        SiteEntity site = siteOpt.get();

        Optional<MovieEntity> movieOpt = movies.findBySiteAndPageId(site, "movie_one");
        assertTrue(movieOpt.isPresent(), "Expected movie not found");
        MovieEntity movie = movieOpt.get();

        Optional<SeasonEntity> seasonOpt = seasons.findByMovieAndNumber(movie, 1);
        assertTrue(seasonOpt.isPresent(), "Expected season not found");

        SeasonEntity season = seasonOpt.get();
        assertEquals(1, season.getNumber());
        assertEquals(movie, season.getMovie());

        assertTrue(seasons.findByMovieAndNumber(movie, 2).isEmpty(), "Unexpected season found");
    }

    @Test
    public void findBySeasonAndNumberTest() {
        assertNotNull(sites);
        assertNotNull(movies);
        assertNotNull(seasons);
        assertNotNull(episodes);

        Optional<SiteEntity> siteOpt = sites.findByAddress(URI.create("https://site.one"));
        assertTrue(siteOpt.isPresent(), "Expected site not found");
        SiteEntity site = siteOpt.get();

        Optional<MovieEntity> movieOpt = movies.findBySiteAndPageId(site, "movie_one");
        assertTrue(movieOpt.isPresent(), "Expected movie not found");
        MovieEntity movie = movieOpt.get();

        Optional<SeasonEntity> seasonOpt = seasons.findByMovieAndNumber(movie, 1);
        assertTrue(seasonOpt.isPresent(), "Expected season not found");
        SeasonEntity season = seasonOpt.get();

        Optional<EpisodeEntity> episodeOpt = episodes.findBySeasonAndNumber(season, 11);
        assertTrue(episodeOpt.isPresent(), "Expected episode not found");

        EpisodeEntity episode = episodeOpt.get();
        assertEquals(11, episode.getNumber());
        assertEquals(season, episode.getSeason());
        assertEquals(movie, episode.getSeason().getMovie());

        assertTrue(episodes.findBySeasonAndNumber(season, 20).isEmpty(), "Unexpected episode found");
    }

    @Test
    public void findByMovieTest() {
        assertNotNull(sites);
        assertNotNull(movies);
        assertNotNull(favorites);

        Optional<SiteEntity> siteOpt = sites.findByAddress(URI.create("https://site.two"));
        assertTrue(siteOpt.isPresent(), "Expected site not found");
        SiteEntity site = siteOpt.get();

        Optional<MovieEntity> movieOpt = movies.findBySiteAndPageId(site, "movie_one");
        assertTrue(movieOpt.isPresent(), "Expected movie not found");
        MovieEntity movieOne = movieOpt.get();

        Optional<FavoriteEntity> favoriteMovieOpt = favorites.findByMovie(movieOne);
        assertTrue(favoriteMovieOpt.isPresent(), "No favorites found by movie");

        FavoriteEntity favorite = favoriteMovieOpt.get();
        assertEquals(movieOne, favorite.getMovie(), "Unexpected movie returned as favorite");

        Optional<MovieEntity> movieThreeOpt = movies.findBySiteAndPageId(site, "movie_three");
        assertTrue(movieThreeOpt.isPresent(), "Expected movie not found");
        MovieEntity movieThree = movieThreeOpt.get();
        assertTrue(favorites.findByMovie(movieThree).isEmpty(), "Unexpected favorite movie found but should not");
    }

    @Test
    public void existsByMovieTest() {
        assertNotNull(sites);
        assertNotNull(movies);
        assertNotNull(favorites);

        Optional<SiteEntity> siteOpt = sites.findByAddress(URI.create("https://site.two"));
        assertTrue(siteOpt.isPresent(), "Expected site not found");
        SiteEntity site = siteOpt.get();

        Optional<MovieEntity> movieOpt = movies.findBySiteAndPageId(site, "movie_one");
        assertTrue(movieOpt.isPresent(), "Expected movie not found");
        MovieEntity movieOne = movieOpt.get();

        assertTrue(favorites.existsByMovie(movieOne), "Expected favorite movie not exists");

        Optional<MovieEntity> movieThreeOpt = movies.findBySiteAndPageId(site, "movie_three");
        assertTrue(movieThreeOpt.isPresent(), "Expected movie not found");
        MovieEntity movieThree = movieThreeOpt.get();
        assertFalse(favorites.existsByMovie(movieThree),
                "Positive result has been achieved instead of negative one");
    }

}