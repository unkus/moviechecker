package moviechecker.database.episode;

import moviechecker.core.di.Episode;
import moviechecker.core.di.EpisodeRepository;
import moviechecker.core.di.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EpisodeRepositoryImpl implements EpisodeRepository {

    private @Autowired EpisodeH2CrudRepository repository;

    @Override
    public Iterable<? extends Episode> getReleased() {
        return repository.findAllByStateNotOrderByDateDesc(State.EXPECTED);
    }

    @Override
    public Iterable<? extends Episode> getExpected() {
        return repository.findAllByStateOrderByDateAsc(State.EXPECTED);
    }
}
