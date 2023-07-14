package moviechecker.ui.favorites;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import moviechecker.di.Favorite;

public class FavoriteView extends JPanel {

	public FavoriteView(final Favorite favorite, FavoriteViewController controller) {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new BorderLayout(0, 0));
		
		JPanel titlePanel = new JPanel();
		add(titlePanel, BorderLayout.CENTER);
		titlePanel.setLayout(new BorderLayout(0, 0));

		JLabel titleLabel = new JLabel(favorite.getTitle());
		titlePanel.add(titleLabel);

		JPanel actionPanel = new JPanel();
		add(actionPanel, BorderLayout.EAST);
		actionPanel.setLayout(new BorderLayout(0, 0));
		
		JButton openButton = new JButton("Открыть");
		actionPanel.add(openButton);
		openButton.addActionListener(event -> controller.onClick$Open(favorite));
		
		JButton removeButton = new JButton("Забыть");
		actionPanel.add(removeButton, BorderLayout.EAST);
		removeButton.addActionListener(event -> controller.removeFromFavorites(favorite));
	}

}
