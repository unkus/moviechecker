package moviechecker.core.di;

public interface EpisodeRepository {
    Iterable<? extends Episode> getReleased();

    Iterable<? extends Episode> getExpected();
}
