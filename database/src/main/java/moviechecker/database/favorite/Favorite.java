package moviechecker.database.favorite;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

import jakarta.persistence.*;
import moviechecker.database.Linkable;
import moviechecker.database.episode.Episode;
import moviechecker.database.movie.Movie;

@Entity
@Table(name = "favorite")
public class Favorite implements Linkable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "movie_id", unique = true, nullable = false, updatable = false)
	private Movie movie;
	
	@OneToOne(optional = true)
	@JoinColumn(name = "episode_id")
	private Episode lastViewed;
	
	/**
	 * @deprecated Only for hibernate usage.
	 */
	@Deprecated
	public Favorite() {
		super();
	}
	
	public Favorite(Movie movie) {
		this.movie = movie;
	}
	
	public Movie getMovie() {
		return movie;
	}

	public Optional<Episode> getLastViewed() {
		return Optional.ofNullable(lastViewed);
	}

	public void setLastViewed(Episode lastViewed) {
		this.lastViewed = lastViewed;
	}

	@Override
	public String toString() {
		return movie.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(movie);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Favorite other = (Favorite) obj;
		return Objects.equals(movie, other.movie);
	}

	@Transient
	@Override
	public URI getLink() {
		return movie.getLink();
	}
}
