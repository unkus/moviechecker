package moviechecker.database.episode;

import java.util.Optional;

import moviechecker.core.di.State;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import moviechecker.database.season.SeasonEntity;

public interface EpisodeH2CrudRepository extends CrudRepository<EpisodeEntity, Long> {

//	@Query("SELECT e FROM Episode e WHERE e.state = :state ORDER BY e.date ASC")
	Iterable<EpisodeEntity> findAllByStateOrderByDateAsc(State state);
	
//	@Query("SELECT e FROM Episode e WHERE e.state = :state ORDER BY e.date DESC")
	Iterable<EpisodeEntity> findAllByStateNotOrderByDateDesc(State state);

//	@Query("SELECT e FROM Episode e JOIN Season s ON s.id = e.season_id WHERE e.number = :number")
	Optional<EpisodeEntity> findBySeasonAndNumber(SeasonEntity season, int number);

//	@Query("SELECT e FROM Episode e WHERE e.state = VIEWED ORDER BY e.date DESC LIMIT 1")
//	Optional<EpisodeEntity> findLastViewed();

//	@Query("DELETE FROM Episode e WHERE e.state = VIEWED AND e.number < (SELECT MAX(e1.number) FROM Episode e1 WHERE e1.state = VIEWED)")
	@Query("DELETE FROM EpisodeEntity")
	void deleteViewedExceptLast();
}
