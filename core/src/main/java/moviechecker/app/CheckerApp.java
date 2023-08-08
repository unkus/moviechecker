package moviechecker.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import moviechecker.ui.MainView;

@SpringBootApplication(scanBasePackages = "moviechecker")
@EnableJpaRepositories(basePackages = "moviechecker")
@EntityScan(basePackages = "moviechecker")
public class CheckerApp implements ApplicationRunner {

	private @Autowired MainView view;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		javax.swing.SwingUtilities.invokeLater(() -> {
			view.setVisible(true);
		});
	}

}
