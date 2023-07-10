package moviechecker;

import moviechecker.database.State;
import moviechecker.datasource.provider.AbstractMovieProvider;
import moviechecker.datasource.provider.DataRecord;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.LocalDateTime;

@Component
public class TestProvider extends AbstractMovieProvider {
    @Override
    public void retrieveData() {
        DataRecord.Builder record = new DataRecord.Builder();
        record.site(URI.create("https://test.test"))
                .moviePageId("movie")
                .movieTitle("movie")
                .moviePath(URI.create("https://test.test/movie"))
                .moviePosterLink(URI.create("https://test.test/poster_one.jpg"))
                .seasonNumber(1)
                .seasonPath(URI.create("https://test.test/movie/season"))
                .episodeNumber(1)
                .episodeTitle("episode 1")
                .episodePath(URI.create("https://test.test/movie/season/episode"))
                .episodeState(State.EXPECTED)
                .episodeDate(LocalDateTime.now());
        try {
            saveRecord(record.build());
        } catch (Exception e) {
            // Doing something
            throw new RuntimeException(e);
        }
    }
}
