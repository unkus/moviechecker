package moviechecker.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import moviechecker.model.Movie;
import moviechecker.model.Season;

public interface SeasonRepository extends CrudRepository<Season, Long> {

//	@Query("SELECT s FROM Season s WHERE e.movie = :movie and s.number = :number")
	public Optional<Season> findByMovieAndNumber(Movie movie, int number);
}
