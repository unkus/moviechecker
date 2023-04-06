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
import moviechecker.model.State;
import javax.swing.SwingConstants;

public class LatestEpisodeView extends JPanel {

	private Logger logger = LoggerFactory.getLogger(LatestEpisodeView.class);

	private static DateTimeFormatter todayFormat = DateTimeFormatter.ofPattern("Сегодня в HH:mm");
	private static DateTimeFormatter yesterdayFormat = DateTimeFormatter.ofPattern("Вчера в HH:mm");
	private static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("d-MM-yyyy, HH:mm");

	public LatestEpisodeView(final Episode episode, EpisodeViewController controller) {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new BorderLayout(0, 0));

		JPanel flagPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) flagPanel.getLayout();
		flowLayout.setAlignOnBaseline(true);
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(flagPanel, BorderLayout.WEST);

		JCheckBox favoriteCheckBox = new JCheckBox();
		flagPanel.add(favoriteCheckBox);

		favoriteCheckBox.setSelected(controller.isInFavorites(episode));
		favoriteCheckBox.addActionListener(event -> {
			JCheckBox cb = (JCheckBox) event.getSource();
			if (cb.isSelected()) {
				controller.addToFavorites(episode.getMovie());
			} else {
				controller.removeFromFavorites(episode.getMovie());
			}
		});

		JPanel titlePanel = new JPanel();
		add(titlePanel, BorderLayout.CENTER);
		titlePanel.setLayout(new BorderLayout(0, 0));
		
		JLabel titleLabel = new JLabel((String) null);
		titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		titleLabel.setText(episode.getSeason().getTitle());
		titlePanel.add(titleLabel, BorderLayout.WEST);
		
		JLabel dateLabel = new JLabel("Нестабильно");
		dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		titlePanel.add(dateLabel, BorderLayout.EAST);
		
		episode.getDate().ifPresent(date -> {
			if (date.isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.MIN))) {
				dateLabel.setText(date.format(todayFormat));
			} else if (date.isAfter(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN))) {
				dateLabel.setText(date.format(yesterdayFormat));
			} else {
				dateLabel.setText(date.format(dateTimeFormat));
			}
		});

		JPanel actionPanel = new JPanel();
		add(actionPanel, BorderLayout.EAST);
		actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton gotoButton = new JButton(
				episode.getState().equals(State.VIEWED) ? "Просмотрено" : episode.getTitle());
		actionPanel.add(gotoButton);
		gotoButton.addActionListener(event -> {
			controller.openInBrowser(episode);
			controller.markViewed(episode);
			((JButton) event.getSource()).setText("Просмотрено");
		});
	}

}
