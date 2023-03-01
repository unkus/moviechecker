package moviechecker.view;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import moviechecker.controller.EpisodeViewController;
import moviechecker.controller.FavoriteViewController;
import moviechecker.controller.MainViewController;
import moviechecker.event.DataErrorEvent;
import moviechecker.event.DataReceivedEvent;
import moviechecker.event.DataRequestedEvent;
import moviechecker.event.FavoriteAddedEvent;
import moviechecker.event.FavoriteRemovedEvent;
import moviechecker.model.FavoriteMovie;
import moviechecker.model.State;
import moviechecker.repository.EpisodeRepository;
import moviechecker.repository.FavoriteRepository;
import java.awt.FlowLayout;

@Component
public class MainView extends JFrame {

	private final Logger logger = LoggerFactory.getLogger(MainView.class);

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

	@PostConstruct
	public void postConstruct() {
		favorites.findAll().forEach(favorite -> {
			favoritesPanel.add(new FavoriteView(favorite, favoriteViewController));
		});
	}
	
	@EventListener
	public void handleDataReceive(DataReceivedEvent event) {
		latestPanel.removeAll();
		episodes.findAllByStateNotOrderByReleaseDateDesc(State.EXPECTED).forEach(episode -> {
			latestPanel.add(new LatestEpisodeView(episode, episodeViewController));
		});

		expectedPanel.removeAll();
		episodes.findAllByStateOrderByReleaseDateAsc(State.EXPECTED).forEach(episode -> {
			expectedPanel.add(new ExpectedEpisodeView(episode, episodeViewController));
		});

		contentPane.validate();
	}

	@EventListener
	public void handleFavoriteAdd(FavoriteAddedEvent event) {
		FavoriteMovie favorite = (FavoriteMovie) event.getSource();
		
		favoritesPanel.add(new FavoriteView(favorite, favoriteViewController));
		// TODO: проставить флаг
		
		contentPane.validate();
	}

	@EventListener
	public void handleFavoriteRemove(FavoriteRemovedEvent event) {
//		Favorite favorite = (Favorite) event.getSource();
//		
//		Stream.of(favoritesPanel.getComponents()).forEach(comp -> {
//			if(comp instanceof FavoriteView) {
//				// TODO: найти у далить нужный
//				// TODO: убрать флаг
//			}
//		});
		
		favoritesPanel.removeAll();
		favorites.findAll().forEach(favorite -> {
			favoritesPanel.add(new FavoriteView(favorite, favoriteViewController));
		});
		
		contentPane.validate();
	}
	
	@EventListener
	public void handleDataError(DataErrorEvent event) {
		JOptionPane.showMessageDialog(contentPane, event.getSource(), "Data error", JOptionPane.ERROR_MESSAGE);
	}
}
