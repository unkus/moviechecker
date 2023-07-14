package moviechecker.ui.episodes;

import moviechecker.di.Episode;
import org.springframework.context.event.EventListener;

public class ReleasedEpisodeView extends EpisodeView {

	public ReleasedEpisodeView(final EpisodeViewController controller) {
		super(controller);
	}

	@Override
	public void onClick$Open(final Episode episode) {
		super.onClick$Open(episode);
		controller.markViewed(episode);
	}

	@EventListener
	public void handleEpisodeViewed(EpisodeViewedEvent event) {
		bind((Episode) event.getSource());
	}
}
