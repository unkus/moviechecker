package moviechecker;

import moviechecker.database.di.DataRecord;
import moviechecker.datasource.provider.MovieProvider;
import moviechecker.di.State;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Component
public class TestProvider implements MovieProvider {
    @Override
    public List<DataRecord> retrieveData() {
        List<DataRecord> recordList = new LinkedList<>();

        DataRecord.Builder record = new DataRecord.Builder();
        record.site(URI.create("https://test.test"))
                .moviePageId("movie")
                .movieTitle("movie")
                .moviePath("/movie")
                .moviePosterLink(URI.create("/poster_one.jpg"))
                .seasonNumber(1)
                .seasonPath("/movie/season")
                .episodeNumber(1)
                .episodeTitle("episode 1")
                .episodePath("/movie/season/episode")
                .episodeState(State.EXPECTED)
                .episodeDate(LocalDateTime.now());

        try {
            recordList.add(record.build());
        } catch (Exception e) {
            // Doing something
            throw new RuntimeException(e);
        }
        return recordList;
    }
}
