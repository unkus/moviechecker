package moviechecker.ui.episodes;

import java.awt.BorderLayout;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import moviechecker.database.episode.Episode;

public class LatestEpisodeView extends EpisodeView {

	public LatestEpisodeView(final EpisodeViewController controller) {
		super(controller);
	}

	@Override
	protected void onOpenButtonClicked(final EpisodeViewController controller, final Episode episode) {
		super.onOpenButtonClicked(controller, episode);
		controller.markViewed(episode);
	}
}
