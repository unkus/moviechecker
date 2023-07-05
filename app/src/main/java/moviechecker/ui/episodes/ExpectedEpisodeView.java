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

public class ExpectedEpisodeView extends JPanel {

	private final Logger logger = LoggerFactory.getLogger(ExpectedEpisodeView.class);

	private static DateTimeFormatter todayFormat = DateTimeFormatter.ofPattern("Сегодня в HH:mm");
	private static DateTimeFormatter tomorrowFormat = DateTimeFormatter.ofPattern("Завтра в HH:mm");
	private static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("d-MM-yyyy, HH:mm");

	public ExpectedEpisodeView(final Episode episode, EpisodeViewController controller) {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new BorderLayout(0, 0));

		JPanel flagPanel = new JPanel();
		add(flagPanel, BorderLayout.WEST);
		flagPanel.setLayout(new BorderLayout(0, 0));

		JLabel dateLabel = new JLabel("Нестабильно");
		flagPanel.add(dateLabel, BorderLayout.WEST);

		episode.getDate().ifPresent(date -> {
			if (date.isBefore(LocalDateTime.of(LocalDate.now(), LocalTime.MAX))) {
				dateLabel.setText(date.format(todayFormat));
			} else if (date.isBefore(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MAX))) {
				dateLabel.setText(date.format(tomorrowFormat));
			} else {
				dateLabel.setText(date.format(dateTimeFormat));
			}
		});

		JPanel titlePanel = new JPanel();
		add(titlePanel, BorderLayout.CENTER);
		titlePanel.setLayout(new BorderLayout(0, 0));

		JCheckBox favoriteCheckBox = new JCheckBox();
		titlePanel.add(favoriteCheckBox, BorderLayout.WEST);

		favoriteCheckBox.setSelected(controller.isInFavorites(episode));
		favoriteCheckBox.addActionListener(event -> {
			JCheckBox cb = (JCheckBox) event.getSource();
			if (cb.isSelected()) {
				controller.addToFavorites(episode.getMovie());
			} else {
				controller.removeFromFavorites(episode.getMovie());
			}
		});

		JLabel titleLabel = new JLabel(episode.getSeason().getTitle());
		titlePanel.add(titleLabel, BorderLayout.CENTER);

		JPanel actionPanel = new JPanel();
		add(actionPanel, BorderLayout.EAST);
		actionPanel.setLayout(new BorderLayout(0, 0));

		JButton openButton = new JButton(episode.getTitle());
		actionPanel.add(openButton);
		openButton.addActionListener(event -> controller.openInBrowser(episode));
	}

}