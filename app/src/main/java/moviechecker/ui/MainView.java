package moviechecker.ui;

import jakarta.annotation.PostConstruct;
import moviechecker.database.episode.EpisodeEntity;
import moviechecker.database.favorite.FavoriteEntity;
import moviechecker.di.CheckerDatabase;
import moviechecker.di.Episode;
import moviechecker.di.Favorite;
import moviechecker.di.State;
import moviechecker.database.episode.EpisodeRepository;
import moviechecker.database.favorite.FavoriteRepository;
import moviechecker.di.events.*;
import moviechecker.ui.episodes.EpisodeView;
import moviechecker.ui.episodes.EpisodeViewController;
import moviechecker.ui.favorites.FavoriteViewController;
import moviechecker.ui.episodes.ReleasedEpisodeView;
import moviechecker.ui.favorites.FavoriteView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.StreamSupport;

@Component
public class MainView extends JFrame {

    private JPanel contentPane;

    private @Autowired ApplicationEventPublisher applicationEventPublisher;

    private @Autowired MainViewController mainViewController;
    private @Autowired EpisodeViewController episodeViewController;
    private @Autowired FavoriteViewController favoriteViewController;

    private @Autowired EpisodeRepository episodeRepository;
    private @Autowired FavoriteRepository favoriteRepository;

    private @Autowired CheckerDatabase database;

    private JPanel releasedPanel;
    private JPanel expectedPanel;
    private JPanel favoritesPanel;

    public MainView() {
        initComponent();
    }

    private void initComponent() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);

        contentPane = new JPanel();
        contentPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane);

        JScrollPane latestScrollPane = new JScrollPane();
        latestScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        tabbedPane.addTab("Последнее", null, latestScrollPane, null);

        releasedPanel = new JPanel();
        latestScrollPane.setViewportView(releasedPanel);
        releasedPanel.setLayout(new BoxLayout(releasedPanel, BoxLayout.Y_AXIS));

        JScrollPane expectedScrollPane = new JScrollPane();
        expectedScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        tabbedPane.addTab("Ожидаемое", null, expectedScrollPane, null);

        expectedPanel = new JPanel();
        expectedScrollPane.setViewportView(expectedPanel);
        expectedPanel.setLayout(new BoxLayout(expectedPanel, BoxLayout.Y_AXIS));

        JScrollPane favoritesScrollPane = new JScrollPane();
        favoritesScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        tabbedPane.addTab("Избранное", null, favoritesScrollPane, null);

        favoritesPanel = new JPanel();
        favoritesScrollPane.setViewportView(favoritesPanel);
        favoritesPanel.setLayout(new BoxLayout(favoritesPanel, BoxLayout.Y_AXIS));

        JPanel actionPanel = new JPanel();
        actionPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        contentPane.add(actionPanel, BorderLayout.EAST);

        JButton checkButton = new JButton("Проверить");
        checkButton.addActionListener(event -> mainViewController.onClick$RequestData());
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.add(checkButton);
    }

    private void updateView() {
        Iterable<Episode> released = episodeRepository.findAllByStateNotOrderByDateDesc(State.EXPECTED);
        StreamSupport.stream(released.spliterator(), false)
                .map(episodeViewController::getView)
                .forEach(releasedPanel::add);

//        released.forEach(episode -> {
//            EpisodeView view = episodeViewController.getView(episode);
//            view.bind(episode);
//            releasedPanel.add(view);
//        });

        Iterable<Episode> expected = episodeRepository.findAllByStateOrderByDateAsc(State.EXPECTED);
        StreamSupport.stream(expected.spliterator(), false)
                .map(episodeViewController::getView)
                .forEach(expectedPanel::add);

//        expected.forEach(episode -> {
//            EpisodeView view = episodeViewController.getView(episode);
//            view.bind(episode);
//            expectedPanel.add(view);
//        });

        contentPane.validate();
    }

    @PostConstruct
    public void postConstruct() {
        database.cleanup();

        StreamSupport.stream(favoriteRepository.findAll().spliterator(), false)
                .map(favorite -> new FavoriteView(favorite, favoriteViewController))
                .forEach(favoritesPanel::add);

//        favoriteRepository.findAll().forEach(favorite -> favoritesPanel.add(new FavoriteView(favorite, favoriteViewController)));

        updateView();
    }

    @EventListener
    public void handleDataReceive(DataReceivedEvent event) {
        SwingUtilities.invokeLater(() -> {
            releasedPanel.removeAll();
            expectedPanel.removeAll();
            updateView();
        });
    }

    @EventListener
    public void handleFavoriteAdd(FavoriteAddedEvent event) {
        SwingUtilities.invokeLater(() -> {
            Favorite favorite = (Favorite) event.getSource();

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
//					// TODO: найти и удалить нужный
//					// TODO: убрать флаг
//				}
//			});

            favoritesPanel.removeAll();

            favoriteRepository.findAll().forEach(favorite -> {
                FavoriteView view = new FavoriteView(favorite, favoriteViewController);
                favoritesPanel.add(view);
            });

            contentPane.validate();
        });
    }

    @EventListener
    public void handleDataError(DataErrorEvent event) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(contentPane, event.getSource(), "Data error", JOptionPane.ERROR_MESSAGE));
    }
}
