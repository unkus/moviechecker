package moviechecker.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import moviechecker.repository.EpisodeRepository;
import moviechecker.repository.FavoriteRepository;
import moviechecker.repository.MovieRepository;
import moviechecker.view.MainView;

@SpringBootApplication(scanBasePackages = "moviechecker")
@EnableJpaRepositories(basePackages = "moviechecker")
@EntityScan(basePackages = "moviechecker")
public class CheckerApp implements ApplicationRunner {

	@Autowired
	private MainView view;
	
	@Autowired
	private FavoriteRepository favorites;
	
	@Autowired
	private MovieRepository movies;
	
	@Autowired
	private EpisodeRepository episodes;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		episodes.findAll().forEach(episode -> {
			if (!favorites.existsByLastViewed(episode)) {
				episodes.delete(episode);
			}
		});
		
		movies.findAll().forEach(movie -> {
			if(!favorites.existsByMovie(movie)) {
				movies.delete(movie);
			}
		});
		
		javax.swing.SwingUtilities.invokeLater(() -> {
            try {
            	view.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
	}

}
