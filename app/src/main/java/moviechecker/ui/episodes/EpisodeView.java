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

    private static final DateTimeFormatter yesterdayFormat = DateTimeFormatter.ofPattern("Вчера в HH:mm");
    private static final DateTimeFormatter todayFormat = DateTimeFormatter.ofPattern("Сегодня в HH:mm");
    private static final DateTimeFormatter tomorrowFormat = DateTimeFormatter.ofPattern("Завтра в HH:mm");
    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("d-MM-yyyy, HH:mm");

    private final EpisodeViewController controller;

    private JLabel dateLabel;
    private JCheckBox favoriteCheckBox;
    private JLabel titleLabel;
    private JButton openButton;

    public EpisodeView(EpisodeViewController controller) {
        this.controller = controller;

        initComponent();
    }

    private void initComponent() {
        setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        setLayout(new BorderLayout(0, 0));

        JPanel datePanel = new JPanel();
        add(datePanel, BorderLayout.WEST);
        datePanel.setLayout(new BorderLayout(0, 0));

        dateLabel = new JLabel();
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        datePanel.add(dateLabel, BorderLayout.EAST);

        JPanel titlePanel = new JPanel();
        add(titlePanel, BorderLayout.CENTER);
        titlePanel.setLayout(new BorderLayout(0, 0));

        favoriteCheckBox = new JCheckBox();
        titlePanel.add(favoriteCheckBox, BorderLayout.WEST);

        titleLabel = new JLabel((String) null);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        add(actionPanel, BorderLayout.EAST);
        actionPanel.setLayout(new BorderLayout(0, 0));

        openButton = new JButton();
        actionPanel.add(openButton);
    }

    public void bind(Episode episode) {
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

        favoriteCheckBox.setSelected(controller.isInFavorites(episode));
        favoriteCheckBox.addActionListener(event -> {
            JCheckBox cb = (JCheckBox) event.getSource();
            if (cb.isSelected()) {
                controller.onClick$AddToFavorites(episode.getMovie());
            } else {
                controller.onClick$RemoveFromFavorites(episode.getMovie());
            }
        });

        titleLabel.setText(episode.getSeason().getTitle());
        openButton.setText((episode.getState().equals(State.VIEWED) ? "✓" : "") + episode.getTitle());
        openButton.addActionListener(event -> onClick$Open(controller, episode));
    }

    protected void onClick$Open(EpisodeViewController controller, Episode episode) {
        controller.onClick$Open(episode);
    }
}
