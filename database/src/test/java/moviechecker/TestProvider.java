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
                .movieLink(URI.create("/movie"))
                .seasonNumber(1)
                .episodeNumber(1)
                .episodeTitle("episode 1")
                .episodeLink(URI.create("/movie/episode"))
                .episodeState(State.EXPECTED)
                .episodeDate(LocalDateTime.now());
        saveRecord(record.build());
    }
}
