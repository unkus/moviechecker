package moviechecker.database.season;

import java.util.Optional;

import moviechecker.database.movie.Movie;
import org.springframework.data.repository.CrudRepository;

public interface SeasonRepository extends CrudRepository<Season, Long> {

//	@Query("SELECT s FROM Season s WHERE s.movie = :movie and s.number = :number")
	Optional<Season> findByMovieAndNumber(Movie movie, int number);
}
