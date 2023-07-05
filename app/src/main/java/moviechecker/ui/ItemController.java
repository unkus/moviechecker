package moviechecker.ui;

import moviechecker.database.episode.Episode;
import org.springframework.beans.factory.annotation.Autowired;

public class ItemController {

    @Autowired private Tools tools;

    public void openInBrowser(Episode episode) {
        tools.openInBrowser(episode.getMovie().getSite().getLink().resolve(episode.getSeason().getLink().getPath().replace(".html", "") + episode.getLink()));
    }
}
