package moviechecker.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Tools {

	private Logger logger = LoggerFactory.getLogger(Tools.class);
	
	public void openInBrowser(URI uri) {
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException ex) {
				logger.error(ex.getLocalizedMessage(), ex);
			}
		} else {
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("xdg-open " + uri);
			} catch (IOException ex) {
				logger.error(ex.getLocalizedMessage(), ex);
			}
		}
	}
	
}
