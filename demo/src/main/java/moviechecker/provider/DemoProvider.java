package moviechecker.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import moviechecker.model.Episode;
import moviechecker.model.Movie;
import moviechecker.model.Season;
import moviechecker.model.Site;
import moviechecker.model.State;
import moviechecker.repository.EpisodeRepository;
import moviechecker.repository.MovieRepository;
import moviechecker.repository.SeasonRepository;
import moviechecker.repository.SiteRepository;

@Component
public class DemoProvider extends AbstractMovieProvider {

	@Autowired
	private SiteRepository sites;

	@Autowired
	private MovieRepository movies;

	@Autowired
	private SeasonRepository seasones;

	@Autowired
	private EpisodeRepository episodes;

	private static final String SITE = "https://some.site";
	// /<movie>.html
	private static final String MOVIE_PAGE_FORMAT = "/%s.html";
	// /<movie>/<season>.html
	private static final String SEASON_PAGE_FORMAT = "/%s/%d.html";
	// /<movie>/<season>/<episode>.html
	private static final String EPISODE_PAGE_FORMAT = "/%s/%d/%d.html";

	// @formatter:off
	// <movie><title><a href="/first-movie.html">First movie</a></title><episode><a href="/first-movie/1/13.html">Серия 13</a></episode><date>Сегодня, 12:23</date></movie>
	// @formatter:on
	private static final Pattern MOVIE_TAG_PATTERN = Pattern.compile(
			"(?:<title><a href=\"(?<movieHref>.*)\">(?<movieTitle>.*)</a></title>)(?:<episode><a href=\"(?<episodeHref>.*)\">(?<episodeTitle>.*)</a></episode>)(?:<date>(?<date>.*)</date>)");

	private static final Pattern MOVIE_HREF_PATTERN = Pattern.compile("/(?<pageId>.*).html");
	private static final Pattern EPISODE_HREF_PATTERN = Pattern
			.compile("/(?:.*)/(?<season>\\d+)/(?<episode>\\d+).html");

	private static final Pattern RELEASE_DATE_PATTERN = Pattern
			.compile("(?<date>Новая серия в|Сегодня|Вчера|\\d?\\d-\\d{2}-\\d{4})(,? (?<time>\\d{2}:\\d{2}))?");

	private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("d-MM-yyyy");

	private BufferedReader createHtmlReader() throws IOException {
		return new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/data.html")));
	}

	@Override
	public void retrieveData() throws Exception {
		URI address = URI.create(SITE);

		// Находим или создаем запись для сайта
		Site site = sites.findByAddress(address).orElse(new Site(address));
		sites.save(site);

		BufferedReader reader = createHtmlReader();
		try {
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				Matcher m = MOVIE_TAG_PATTERN.matcher(inputLine);
				if (m.find()) {
					String movieHref = m.group("movieHref");
					String movieTitle = m.group("movieTitle");

					String pageId = MOVIE_HREF_PATTERN.matcher(movieHref).group("pageId");
					Movie movie = movies.findBySiteAndPageId(site, pageId).orElse(new Movie(site, pageId));
					movie.setTitle(movieTitle);
					movie.setLink(URI.create(movieHref));
					movies.save(movie);

					String episodeHref = m.group("episodeHref");
					Matcher episodeHrefMatcher = EPISODE_HREF_PATTERN.matcher(episodeHref);
					int seasonNumber = Integer.parseInt(episodeHrefMatcher.group("season"));
					int episodeNumber = Integer.parseInt(episodeHrefMatcher.group("episode"));

					Season season = seasones.findByMovieAndNumber(movie, seasonNumber)
							.orElse(new Season(movie, seasonNumber));
					season.setTitle(movieTitle + " " + seasonNumber);
					season.setLink(URI.create(inputLine));
					seasones.save(season);

					Episode episode = episodes.findBySeasonAndNumber(season, episodeNumber)
							.orElse(new Episode(season, episodeNumber));
					episode.setTitle(m.group("episodeTitle"));
					episode.setLink(URI.create(episodeHref));

					Matcher dateMatcher = RELEASE_DATE_PATTERN.matcher(m.group("date"));
					if (dateMatcher.find()) {
						LocalDate localDate;
						String date = dateMatcher.group("date");
						switch (date) {
						case "Новая серия в":
							episode.setState(State.EXPECTED);
							localDate = LocalDate.now();
							break;
						case "Сегодня":
							episode.setState(State.RELEASED);
							localDate = LocalDate.now();
							break;
						case "Вчера":
							episode.setState(State.RELEASED);
							localDate = LocalDate.now().minusDays(1);
							break;
						default:
                            episode.setState(State.RELEASED);
                            localDate = LocalDate.parse(date, dateFormat);
                        }
                    String time = dateMatcher.group("time");
                    if (time != null) {
                        episode.setDate(LocalDateTime.of(localDate, LocalTime.parse(time)));
                    }
                }

					episodes.save(episode);
				}
			}
		} finally {
			reader.close();
		}

	}

}
