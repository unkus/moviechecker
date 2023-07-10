package moviechecker.ui;

import moviechecker.database.episode.Episode;
import moviechecker.database.movie.Movie;
import moviechecker.database.season.Season;
import moviechecker.database.site.Site;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;

public class ItemController {

    @Autowired private Tools tools;

    public void openInBrowser(Episode episode) {
        Site site = episode.getMovie().getSite();
        Movie movie = episode.getMovie();
        Season season = episode.getSeason();
        URI link = site.getLink().resolve(movie.getPath() + season.getPath() + episode.getPath());
        tools.openInBrowser(link);
    }
}
