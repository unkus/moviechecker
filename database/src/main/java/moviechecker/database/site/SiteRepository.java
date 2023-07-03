package moviechecker.database.site;

import java.net.URI;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface SiteRepository extends CrudRepository<Site, Long> {

//	@Query("SELECT s FROM Site s WHERE s.address = :address")
	Optional<Site> findByAddress(URI address);
}
