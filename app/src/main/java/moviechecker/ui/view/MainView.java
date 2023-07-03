package moviechecker.ui.view;

import jakarta.annotation.PostConstruct;
import moviechecker.database.State;
import moviechecker.database.episode.EpisodeRepository;
import moviechecker.database.favorite.FavoriteMovie;
import moviechecker.database.favorite.FavoriteRepository;
import moviechecker.datasource.event.DataErrorEvent;
import moviechecker.datasource.event.DataReceivedEvent;
import moviechecker.datasource.event.DataRequestedEvent;
import moviechecker.ui.controller.EpisodeViewController;
import moviechecker.ui.controller.FavoriteViewController;
import moviechecker.ui.controller.MainViewController;
import moviechecker.ui.event.FavoriteAddedEvent;
import moviechecker.ui.event.FavoriteRemovedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

@Component
public class MainView extends JFrame {

    private final JPanel contentPane;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private MainViewController mainViewController;

    @Autowired
    private EpisodeViewController episodeViewController;

    @Autowired
    private FavoriteViewController favoriteViewController;

    @Autowired
    private EpisodeRepository episodes;

    @Autowired
    private FavoriteRepository favorites;

    private final JPanel latestPanel;

    private final JPanel expectedPanel;

    private final JPanel favoritesPanel;

    public MainView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane);

        JScrollPane latestScrollPane = new JScrollPane();
        tabbedPane.addTab("Последнее", null, latestScrollPane, null);

        latestPanel = new JPanel();
        latestScrollPane.setViewportView(latestPanel);
        latestPanel.setLayout(new BoxLayout(latestPanel, BoxLayout.Y_AXIS));

        JScrollPane expectedScrollPane = new JScrollPane();
        tabbedPane.addTab("Ожидаемое", null, expectedScrollPane, null);

        expectedPanel = new JPanel();
        expectedScrollPane.setViewportView(expectedPanel);
        expectedPanel.setLayout(new BoxLayout(expectedPanel, BoxLayout.Y_AXIS));

        JScrollPane favoritesScrollPane = new JScrollPane();
        tabbedPane.addTab("Избранное", null, favoritesScrollPane, null);

        favoritesPanel = new JPanel();
        favoritesScrollPane.setViewportView(favoritesPanel);
        favoritesPanel.setLayout(new BoxLayout(favoritesPanel, BoxLayout.Y_AXIS));

        JPanel actionPanel = new JPanel();
        actionPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        contentPane.add(actionPanel, BorderLayout.EAST);

        JButton checkButton = new JButton("Проверить");
        checkButton.addActionListener(event -> {
            applicationEventPublisher.publishEvent(new DataRequestedEvent(this));
        });
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.add(checkButton);
    }

    private void updateView() {
        episodes.findAllByStateNotOrderByDateDesc(State.EXPECTED).forEach(episode -> {
            latestPanel.add(new LatestEpisodeView(episode, episodeViewController));
        });
        episodes.findAllByStateOrderByDateAsc(State.EXPECTED).forEach(episode -> {
            expectedPanel.add(new ExpectedEpisodeView(episode, episodeViewController));
        });
        contentPane.validate();
    }

    @PostConstruct
    public void postConstruct() {
        favorites.findAll().forEach(favorite -> {
            favoritesPanel.add(new FavoriteView(favorite, favoriteViewController));
        });

        updateView();
    }

    @EventListener
    public void handleDataReceive(DataReceivedEvent event) {
        SwingUtilities.invokeLater(() -> {
            latestPanel.removeAll();
            expectedPanel.removeAll();

            updateView();
        });
    }

    @EventListener
    public void handleFavoriteAdd(FavoriteAddedEvent event) {
        SwingUtilities.invokeLater(() -> {
            FavoriteMovie favorite = (FavoriteMovie) event.getSource();

            favoritesPanel.add(new FavoriteView(favorite, favoriteViewController));
            // TODO: проставить флаг

            contentPane.validate();
        });
    }

    @EventListener
    public void handleFavoriteRemove(FavoriteRemovedEvent event) {
        SwingUtilities.invokeLater(() -> {
//			Favorite favorite = (Favorite) event.getSource();
//
//			Stream.of(favoritesPanel.getComponents()).forEach(comp -> {
//				if(comp instanceof FavoriteView) {
//					// TODO: найти удалить нужный
//					// TODO: убрать флаг
//				}
//			});

            favoritesPanel.removeAll();
            favorites.findAll().forEach(favorite -> {
                favoritesPanel.add(new FavoriteView(favorite, favoriteViewController));
            });

            contentPane.validate();
        });
    }

    @EventListener
    public void handleDataError(DataErrorEvent event) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(contentPane, event.getSource(), "Data error", JOptionPane.ERROR_MESSAGE);
        });
    }
}
