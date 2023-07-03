package moviechecker.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import moviechecker.model.Movie;
import moviechecker.model.Season;

public interface SeasonRepository extends CrudRepository<Season, Long> {

//	@Query("SELECT s FROM Season s WHERE s.movie = :movie and s.number = :number")
	Optional<Season> findByMovieAndNumber(Movie movie, int number);
}
