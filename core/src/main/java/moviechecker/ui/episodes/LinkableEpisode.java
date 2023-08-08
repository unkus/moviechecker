package moviechecker.ui.episodes;

import moviechecker.core.di.Episode;
import moviechecker.ui.Linkable;

import java.net.URI;

public class LinkableEpisode extends EpisodeDecorator implements Linkable {
    LinkableEpisode(Episode episode) {
        super(episode);
    }

    @Override
    public final URI getLink() {
        return episode.getSeason().getMovie().getSite().getAddress().resolve(getPath());
    }
}
