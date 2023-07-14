package moviechecker.database.season;

import java.util.Optional;

import moviechecker.database.movie.MovieEntity;
import org.springframework.data.repository.CrudRepository;

public interface SeasonRepository extends CrudRepository<SeasonEntity, Long> {

//	@Query("SELECT s FROM Season s WHERE s.movie = :movie and s.number = :number")
	Optional<SeasonEntity> findByMovieAndNumber(MovieEntity movie, int number);
}
