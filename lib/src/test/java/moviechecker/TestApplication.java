package moviechecker;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import jakarta.annotation.PostConstruct;
import moviechecker.event.DataRequestedEvent;
import moviechecker.model.Episode;
import moviechecker.model.FavoriteMovie;
import moviechecker.model.Movie;
import moviechecker.model.Season;
import moviechecker.model.Site;
import moviechecker.model.State;
import moviechecker.provider.AbstractMovieProvider;
import moviechecker.provider.MovieProvider;
import moviechecker.repository.EpisodeRepository;
import moviechecker.repository.FavoriteRepository;
import moviechecker.repository.MovieRepository;
import moviechecker.repository.SeasonRepository;
import moviechecker.repository.SiteRepository;

@SpringBootApplication
public class TestApplication {

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

    @Bean
    public MovieProvider testProvider() {
        return new MovieProvider() {

            @Override
            public void retrieveData() throws Exception {
            	final int seasonesPerMovieLimit = 2;
            	final int episodesPerSeasonLimit = 4;
            	Stream.of("one", "two").forEach(site_id -> {
                    Site site = new Site(URI.create("https://site." + site_id));
                    sites.save(site);
                    
                    Stream.of("one", "two").forEach(movie_id -> {
                    	Movie movie = new Movie(site,  "movie_" + movie_id);
                        movie.setTitle("Фильм " + movie_id);
                        movie.setLink(URI.create(site.getAddress().toString() + "/" + movie_id + ".html"));
                        movies.save(movie);

                        Stream.iterate(1, value -> value + 1).limit(seasonesPerMovieLimit).forEach(seasonId -> {
                        	Season season = new Season(movie, seasonId);
                        	season.setLink(URI.create(movie.getLink().toString() + "/" + seasonId + ".html"));
                        	seasons.save(season);
                        	
                            Stream.iterate(1, value -> value + 1).limit(episodesPerSeasonLimit).forEach(episodeId -> {
                            	Episode episode = new Episode(season, episodeId);
                            	episode.setTitle("Серия " + episodeId);
                            	episode.setLink(URI.create(season.getLink().toString() + "/" + episodeId + ".html"));
                            	episode.setState(episodeId <=3 ? State.RELEASED : State.EXPECTED);
                            	episode.setReleaseDate(LocalDateTime.now().minusDays(4 - episodeId));
                            	episodes.save(episode);
                            });
                        });
                    });
            	});
            	
            	Movie movie = movies.findById(1L).get();
            	Season season = movie.getSeasons().iterator().next(); // take first
            	Episode episode = episodes.findBySeasonAndNumber(season, 3).get();
            			
                FavoriteMovie favorite = new FavoriteMovie(movie);
            	favorite.setLastViewed(episode);
            	favorites.save(favorite);
            }

			@Override
			public void handleDataRequest(DataRequestedEvent event) {
				// stub
			}

			@PostConstruct
			public void postConstruct() {
				try {
					retrieveData();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

        };
    }
}
