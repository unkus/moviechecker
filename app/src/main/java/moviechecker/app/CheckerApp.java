package moviechecker.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import moviechecker.repository.FavoriteRepository;
import moviechecker.repository.SiteRepository;
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
	private SiteRepository sites;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		databaseCleanup();

		javax.swing.SwingUtilities.invokeLater(() -> {
			view.setVisible(true);
		});
	}

	private void databaseCleanup() {
		sites.findAll().forEach(site -> {
			site.getMovies().removeIf(movie -> !favorites.existsByMovie(movie));
			if (site.getMovies().isEmpty()) {
				sites.delete(site);
			} else {
				sites.save(site);
			}
		});
	}
}
