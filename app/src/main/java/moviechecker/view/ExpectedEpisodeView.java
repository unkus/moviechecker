package moviechecker.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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

import moviechecker.controller.EpisodeViewController;
import moviechecker.model.Episode;

public class ExpectedEpisodeView extends JPanel {

	private final Logger logger = LoggerFactory.getLogger(ExpectedEpisodeView.class);

	public ExpectedEpisodeView(final Episode episode, EpisodeViewController controller) {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new BorderLayout(0, 0));

		JPanel flagPanel = new JPanel();
		add(flagPanel, BorderLayout.WEST);
		flagPanel.setLayout(new BorderLayout(0, 0));

		JLabel dateLabel = new JLabel("Нестабильно");
		flagPanel.add(dateLabel, BorderLayout.WEST);

		episode.getDate().ifPresent(date -> {
			if (date.isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.MIN))) {
				dateLabel.setText(date.format(DateTimeFormatter.ofPattern("Сегодня HH:mm")));
			} else if (date.isAfter(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN))) {
				dateLabel.setText(date.format(DateTimeFormatter.ofPattern("Вчера HH:mm")));
			} else {
				dateLabel.setText(date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
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
