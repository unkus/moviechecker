package moviechecker.ui.episodes;

import moviechecker.database.episode.Episode;

public class ReleasedEpisodeView extends EpisodeView {

	public ReleasedEpisodeView(final EpisodeViewController controller) {
		super(controller);
	}

	@Override
	protected void onClick$Open(final EpisodeViewController controller, final Episode episode) {
		super.onClick$Open(controller, episode);
		controller.markViewed(episode);
	}
}
