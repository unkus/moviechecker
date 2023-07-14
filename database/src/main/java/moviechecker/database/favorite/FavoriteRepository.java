package moviechecker.database.favorite;

import java.util.Optional;

import moviechecker.database.movie.MovieEntity;
import org.springframework.data.repository.CrudRepository;

import moviechecker.database.episode.EpisodeEntity;

public interface FavoriteRepository extends CrudRepository<FavoriteEntity, Long> {

//	@Query("SELECT f FROM FavoriteMovie f WHERE f.movie = :movie")
	Optional<FavoriteEntity> findByMovie(MovieEntity movie);

//	@Query("SELECT CASE WHEN count(f)> 0 THEN true ELSE false END FROM FavoriteMovie f WHERE f.movie = :movie ")
	boolean existsByMovie(MovieEntity movie);

//	@Query("SELECT CASE WHEN count(f)> 0 THEN true ELSE false END FROM FavoriteMovie f WHERE f.lastViewed = :episode ")
	boolean existsByLastViewed(EpisodeEntity episode);
}
