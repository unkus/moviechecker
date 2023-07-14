package moviechecker.database.episode;

import java.util.Optional;

import moviechecker.di.State;
import org.springframework.data.repository.CrudRepository;

import moviechecker.database.season.SeasonEntity;

public interface EpisodeRepository extends CrudRepository<EpisodeEntity, Long> {

//	@Query("SELECT e FROM Episode e WHERE e.state = :state ORDER BY e.date ASC")
	Iterable<EpisodeEntity> findAllByStateOrderByDateAsc(State state);
	
//	@Query("SELECT e FROM Episode e WHERE e.state = :state ORDER BY e.date DESC")
	Iterable<EpisodeEntity> findAllByStateNotOrderByDateDesc(State state);

//	@Query("SELECT e FROM Episode e WHERE e.season = :season AND e.number = :number")
	Optional<EpisodeEntity> findBySeasonAndNumber(SeasonEntity season, int number);

}
