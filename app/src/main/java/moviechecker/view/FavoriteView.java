package moviechecker.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import moviechecker.controller.FavoriteViewController;
import moviechecker.model.FavoriteMovie;

public class FavoriteView extends JPanel {

	public FavoriteView(final FavoriteMovie favorite, FavoriteViewController controller) {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new BorderLayout(0, 0));

		JPanel flagPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) flagPanel.getLayout();
		flowLayout.setAlignOnBaseline(true);
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(flagPanel, BorderLayout.WEST);
		
		JPanel titlePanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) titlePanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		add(titlePanel, BorderLayout.CENTER);

		JLabel titleLabel = new JLabel(favorite.getMovie().getTitle());
		titlePanel.add(titleLabel);

		JPanel actionPanel = new JPanel();
		add(actionPanel, BorderLayout.EAST);
		actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton openButton = new JButton("Открыть");
		actionPanel.add(openButton);
		openButton.addActionListener(event -> {
			controller.openInBrowser(favorite);
		});
		
		JButton removeButton = new JButton("Удалить");
		actionPanel.add(removeButton);
		removeButton.addActionListener(event -> {
			controller.removeFromFavorites(favorite);
		});

	}

}
