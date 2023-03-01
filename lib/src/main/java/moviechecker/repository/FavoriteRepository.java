package moviechecker.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import moviechecker.model.Episode;
import moviechecker.model.FavoriteMovie;
import moviechecker.model.Movie;

public interface FavoriteRepository extends CrudRepository<FavoriteMovie, Long> {

//	@Query("SELECT f FROM FavoriteMovie f WHERE f.movie = :movie")
	public Optional<FavoriteMovie> findByMovie(Movie movie);

//	@Query("SELECT CASE WHEN count(f)> 0 THEN true ELSE false END FROM FavoriteMovie f WHERE f.movie = :movie ")
	public boolean existsByMovie(Movie movie);

//	@Query("SELECT CASE WHEN count(f)> 0 THEN true ELSE false END FROM FavoriteMovie f WHERE f.lastViewed = :episode ")
	public boolean existsByLastViewed(Episode episode);
}
