package moviechecker.database.favorite;

import moviechecker.core.di.Favorite;
import moviechecker.core.di.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FavoriteRepositoryImpl implements FavoriteRepository {
    private @Autowired FavoriteH2CrudRepository repository;

    @Override
    public Iterable<? extends Favorite> getAll() {
        return repository.findAll();
    }
}
