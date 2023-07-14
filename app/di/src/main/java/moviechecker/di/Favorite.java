package moviechecker.di;

import java.util.Optional;

public interface Favorite {
    Movie getMovie();
    Optional<Episode> getLastViewed();
    String getTitle();
}
