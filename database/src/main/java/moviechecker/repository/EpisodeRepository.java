package moviechecker.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import moviechecker.model.Episode;
import moviechecker.model.Season;
import moviechecker.model.State;

public interface EpisodeRepository extends CrudRepository<Episode, Long> {

//	@Query("SELECT e FROM Episode e WHERE e.state = :state ORDER BY e.date ASC")
	Iterable<Episode> findAllByStateOrderByDateAsc(State state);
	
//	@Query("SELECT e FROM Episode e WHERE e.state = :state ORDER BY e.date DESC")
	Iterable<Episode> findAllByStateNotOrderByDateDesc(State state);

//	@Query("SELECT e FROM Episode e WHERE e.season = :season AND e.number = :number")
	Optional<Episode> findBySeasonAndNumber(Season season, int number);

}
