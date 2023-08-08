package moviechecker.core.di;

import java.time.LocalDateTime;

public interface Episode {
    int getNumber();
    Season getSeason();
    String getTitle();
    LocalDateTime getReleaseDate();
    String getPath();
    State getState();
}
