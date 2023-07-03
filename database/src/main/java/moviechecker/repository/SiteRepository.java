package moviechecker.repository;

import java.net.URI;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import moviechecker.model.Site;

public interface SiteRepository extends CrudRepository<Site, Long> {

//	@Query("SELECT s FROM Site s WHERE s.address = :address")
	Optional<Site> findByAddress(URI address);
}
