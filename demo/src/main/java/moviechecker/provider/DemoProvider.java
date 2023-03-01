package moviechecker.provider;

import java.io.BufferedReader;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Matcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import moviechecker.model.Episode;
import moviechecker.model.Movie;
import moviechecker.model.Site;
import moviechecker.model.State;
import moviechecker.repository.EpisodeRepository;
import moviechecker.repository.MovieRepository;
import moviechecker.repository.SiteRepository;

@Component
public class DemoProvider extends AbstractMovieProvider {

	@Autowired
	private SiteRepository sites;

	@Autowired
	private EpisodeRepository episodes;

	@Autowired
	private MovieRepository movies;

	private static final String SITE = "https://some.site";
	private static final String EPISODE_PAGE_PATTERN = "/%d.html";

	@Override
	public void retrieveData() throws Exception {
		URI address = URI.create(SITE);

		// Находим или создаем запись для сайта
		Site site = sites.findByAddress(address).orElse(new Site());
		site.setAddress(address);
		site.setEpisodeLinkPattern(EPISODE_PAGE_PATTERN);
		sites.save(site);

		String page = "/movie.html";
		Movie movie = movies.findBySiteAndPage(site, page).orElse(new Movie());
		movie.setSite(site);
		movie.setPage(page);
		movie.setTitle("Some movie title here");
		movies.save(movie);

		final int number = 11;
		Episode episode = episodes.findBySeasonAndNumber(season, number).orElse(new Episode());

		episode.setMovie(movie);
		episode.setNumber(number);
		episode.setState(State.RELEASED);
		episode.setReleaseDate(LocalDateTime.now());
		episodes.save(episode);
	}

}
