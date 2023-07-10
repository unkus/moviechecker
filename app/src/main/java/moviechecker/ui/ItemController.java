package moviechecker.ui;

import moviechecker.database.Linkable;
import moviechecker.database.episode.Episode;
import moviechecker.database.movie.Movie;
import moviechecker.database.season.Season;
import moviechecker.database.site.Site;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;

public class ItemController {

    @Autowired private Tools tools;

    public void onClick$Open(Linkable obj) {
        tools.openInBrowser(obj.getLink());
    }
}
