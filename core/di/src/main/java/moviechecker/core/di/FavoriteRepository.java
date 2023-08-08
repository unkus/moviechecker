package moviechecker.core.di;

public interface FavoriteRepository {
    Iterable<? extends Favorite> getAll();
}
