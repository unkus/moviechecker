package moviechecker.database.episode;

import java.util.Optional;

import moviechecker.database.State;
import org.springframework.data.repository.CrudRepository;

import moviechecker.database.season.Season;

public interface EpisodeRepository extends CrudRepository<Episode, Long> {

//	@Query("SELECT e FROM Episode e WHERE e.state = :state ORDER BY e.date ASC")
	Iterable<Episode> findAllByStateOrderByDateAsc(State state);
	
//	@Query("SELECT e FROM Episode e WHERE e.state = :state ORDER BY e.date DESC")
	Iterable<Episode> findAllByStateNotOrderByDateDesc(State state);

//	@Query("SELECT e FROM Episode e WHERE e.season = :season AND e.number = :number")
	Optional<Episode> findBySeasonAndNumber(Season season, int number);

}
