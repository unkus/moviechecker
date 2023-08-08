package moviechecker.database.site;

import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;
import moviechecker.database.converters.UriPersistanceConverter;
import moviechecker.database.movie.MovieEntity;
import moviechecker.core.di.Site;

@Entity
@Table(name = "site")
public class SiteEntity implements Site {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, unique = true, updatable = false)
	@Convert(converter = UriPersistanceConverter.class)
	private URI address;

	@OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<MovieEntity> movies = new HashSet<>();

	/**
	 * @deprecated Only for hibernate usage.
	 */
	@Deprecated
	public SiteEntity() {
		super();
	}
	
	public SiteEntity(URI address) {
		this.address = address;
	}
	
	public URI getAddress() {
		return address;
	}

	public Set<MovieEntity> getMovies() {
		return movies;
	}

	@Override
	public String toString() {
		return address.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(address);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SiteEntity other = (SiteEntity) obj;
		return Objects.equals(address, other.address);
	}

}
