package moviechecker.ui.episodes;

import moviechecker.database.State;
import moviechecker.database.episode.Episode;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public abstract class EpisodeView extends JPanel {

    private static DateTimeFormatter yesterdayFormat = DateTimeFormatter.ofPattern("Вчера в HH:mm");
    private static DateTimeFormatter todayFormat = DateTimeFormatter.ofPattern("Сегодня в HH:mm");
    private static DateTimeFormatter tomorrowFormat = DateTimeFormatter.ofPattern("Завтра в HH:mm");
    private static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("d-MM-yyyy, HH:mm");

    public EpisodeView(final Episode episode, EpisodeViewController controller) {
        setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        setLayout(new BorderLayout(0, 0));

        JPanel datePanel = new JPanel();
        add(datePanel, BorderLayout.WEST);
        datePanel.setLayout(new BorderLayout(0, 0));

        JLabel dateLabel = new JLabel();
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        datePanel.add(dateLabel, BorderLayout.EAST);

        episode.getDate().ifPresent(date -> {
            LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
            LocalDateTime yesterday = today.minusDays(1);
            LocalDateTime tomorrow = today.plusDays(1);

            if (date.isBefore(yesterday) || date.isAfter(tomorrow)) {
                dateLabel.setText(date.format(dateTimeFormat));
            } else if (date.isBefore(today)) {
                dateLabel.setText(date.format(yesterdayFormat));
            } else if (date.isBefore(tomorrow)) {
                dateLabel.setText(date.format(todayFormat));
            } else {
                dateLabel.setText(date.format(tomorrowFormat));
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

        JLabel titleLabel = new JLabel((String) null);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setText(episode.getSeason().getTitle());
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        add(actionPanel, BorderLayout.EAST);
        actionPanel.setLayout(new BorderLayout(0, 0));

        JButton gotoButton = new JButton((episode.getState().equals(State.VIEWED) ? "✓" : "") + episode.getTitle());
        actionPanel.add(gotoButton);
        gotoButton.addActionListener(event -> onOpenButtonClicked(controller, episode));
    }

    protected void onOpenButtonClicked(EpisodeViewController controller, Episode episode) {
        controller.openInBrowser(episode);
    }
}
