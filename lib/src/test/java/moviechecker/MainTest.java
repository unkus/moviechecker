package moviechecker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import moviechecker.model.Episode;
import moviechecker.model.FavoriteMovie;
import moviechecker.model.Movie;
import moviechecker.model.Season;
import moviechecker.model.Site;
import moviechecker.model.State;
import moviechecker.provider.MovieProvider;
import moviechecker.repository.EpisodeRepository;
import moviechecker.repository.FavoriteRepository;
import moviechecker.repository.MovieRepository;
import moviechecker.repository.SiteRepository;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class MainTest {

	@Autowired
	private MovieProvider testProvider;
	
	@Autowired
	private SiteRepository sites;
	
	@Autowired
	private MovieRepository movies;
	
	@Autowired
	private EpisodeRepository episodes;
	
	@Autowired
	private FavoriteRepository favorites;
	
	@Test
	public void findAllByStateOrderByReleaseDateAscTest() {
		assertNotNull(episodes);
		
		List<Episode> list = new LinkedList<>();
		episodes.findAllByStateOrderByReleaseDateAsc(State.EXPECTED).forEach(episode -> list.add(episode));
		assertEquals(4, list.size());
		
		Episode last = null;
		int ascendingSequence = 0;
		for (Iterator<Episode> iterator = list.iterator(); iterator.hasNext();) {
			Episode episode = (Episode) iterator.next();
			if(last == null || last.getReleaseDate().get().isBefore(episode.getReleaseDate().get())) {
				last = episode;
				ascendingSequence++;
			}
		}
		
		assertEquals(4, ascendingSequence);
	}
	
	@Test
	public void findAllByStateOrderByReleaseDateDescTest() {
		assertNotNull(episodes);
		
		List<Episode> list = new LinkedList<>();
		episodes.findAllByStateNotOrderByReleaseDateDesc(State.RELEASED).forEach(episode -> list.add(episode));
		assertEquals(12, list.size());
		
		Episode last = null;
		int descendingSequence = 0;
		for (Iterator<Episode> iterator = list.iterator(); iterator.hasNext();) {
			Episode episode = (Episode) iterator.next();
			if(last == null || last.getReleaseDate().get().isAfter(episode.getReleaseDate().get())) {
				last = episode;
				descendingSequence++;
			}
		}
		
		assertEquals(12, descendingSequence);
	}
	
	@Test
	public void findByAddressTest() {
		assertNotNull(sites);
		
		URI address = URI.create("https://site.two");
		Optional<Site> siteOpt = sites.findByAddress(address);
		assertTrue(siteOpt.isPresent(), "Сайт не найден");
		
		Site site = siteOpt.get();
		assertEquals(address, site.getAddress());
	}
	
	@Test
	public void findBySiteAndPageTest() {
		assertNotNull(sites);
		assertNotNull(movies);
		
		Site site = sites.findById(1L).get();
		
		Optional<Movie> movieOpt = movies.findBySiteAndPageId(site, "movie_one");
		assertTrue(movieOpt.isPresent(), "Фильм не найден.");

		Movie movie = movieOpt.get();
		assertEquals(site, movie.getSite());
		assertEquals("/movie_one.html", movie.getPageId());
	}

	@Test
	public void findBySeasonAndNumberTest() {
		assertNotNull(movies);
		assertNotNull(episodes);
		
		Movie movie = movies.findById(1L).get();
		Season season = movie.getSeasons().iterator().next();
		
		Optional<Episode> episodeOpt = episodes.findBySeasonAndNumber(season, 3);
		assertTrue(episodeOpt.isPresent(), "Эпизод не найден.");
		
		Episode episode = episodeOpt.get();
		assertEquals(3, episode.getNumber());
		assertEquals(season, episode.getSeason());
		assertEquals(movie, episode.getMovie());
	}
	
	@Test
	public void findByMovieTest() {
		assertNotNull(movies);
		assertNotNull(favorites);
		
		Movie movie = movies.findById(1L).get();

		Optional<FavoriteMovie> favoriteOpt = favorites.findByMovie(movie);
		assertTrue(favoriteOpt.isPresent(), "Фильм отсутствует в избранном.");
		
		FavoriteMovie favorite = favoriteOpt.get();
		assertEquals(movie, favorite.getMovie());
	}
	
	@Test
	public void existsByMovieTest() {
		assertNotNull(movies);
		assertNotNull(favorites);
		
		Movie movie = movies.findById(1L).get();
		
		assertTrue(favorites.existsByMovie(movie), "Фильм отсутствует в избранном.");
	}
	
	@Test
	public void existsByLastViewedTest() {
		assertNotNull(episodes);
		assertNotNull(favorites);
		
		Movie movie = movies.findById(1L).get();
		Season season = movie.getSeasons().iterator().next();
		Episode episode = episodes.findBySeasonAndNumber(season, 3).get();
		
		assertTrue(favorites.existsByLastViewed(episode), "Фильм отсутствует в избранном.");
	}
	
	@Test
	public void getLinkTest() {
		assertNotNull(episodes);
		
		Episode episode = episodes.findById(1L).get();
		
		assertEquals("https://site.one/movie_one/1.html", episode.getLink().toString());
	}
}
