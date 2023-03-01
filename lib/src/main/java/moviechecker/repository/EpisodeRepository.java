package moviechecker.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import moviechecker.model.Episode;
import moviechecker.model.Season;
import moviechecker.model.State;

public interface EpisodeRepository extends CrudRepository<Episode, Long> {

//	@Query("SELECT e FROM Episode e WHERE e.state = :state ORDER BY e.releaseDate ASC")
	public Iterable<Episode> findAllByStateOrderByReleaseDateAsc(State state);
	
//	@Query("SELECT e FROM Episode e WHERE e.state = :state ORDER BY e.releaseDate DESC")
	public Iterable<Episode> findAllByStateNotOrderByReleaseDateDesc(State state);

//	@Query("SELECT e FROM Episode e WHERE e.season = :season and e.number = :number")
	public Optional<Episode> findBySeasonAndNumber(Season season, int number);

}
