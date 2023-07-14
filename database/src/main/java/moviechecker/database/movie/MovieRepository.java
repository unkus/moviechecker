package moviechecker.database.movie;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import moviechecker.database.site.SiteEntity;

public interface MovieRepository extends CrudRepository<MovieEntity, Long> {

//	@Query("SELECT m FROM Movie m WHERE m.site = :site AND m.pageId = :pageId")
	Optional<MovieEntity> findBySiteAndPageId(SiteEntity site, String pageId);

}
