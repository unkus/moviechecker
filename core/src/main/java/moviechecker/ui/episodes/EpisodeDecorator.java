package moviechecker.ui.episodes;

import moviechecker.core.di.Episode;
import moviechecker.core.di.Season;
import moviechecker.core.di.State;

import java.time.LocalDateTime;

public abstract class EpisodeDecorator implements Episode {
    final Episode episode;

    EpisodeDecorator(Episode episode) {
        this.episode = episode;
    }

    @Override
    public final int getNumber() {
        return episode.getNumber();
    }

    @Override
    public final Season getSeason() {
        return episode.getSeason();
    }

    @Override
    public final String getTitle() {
        return episode.getTitle();
    }

    @Override
    public final LocalDateTime getReleaseDate() {
        return episode.getReleaseDate();
    }

    @Override
    public final String getPath() {
        return episode.getPath();
    }

    @Override
    public final State getState() {
        return episode.getState();
    }
}
