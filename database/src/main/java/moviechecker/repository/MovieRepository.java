package moviechecker.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import moviechecker.model.Movie;
import moviechecker.model.Site;

public interface MovieRepository extends CrudRepository<Movie, Long> {

//	@Query("SELECT m FROM Movie m WHERE m.site = :site AND m.pageId = :pageId")
	Optional<Movie> findBySiteAndPageId(Site site, String pageId);

}
