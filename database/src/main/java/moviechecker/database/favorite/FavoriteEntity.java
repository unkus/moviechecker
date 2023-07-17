package moviechecker.database.favorite;

import jakarta.persistence.*;
import moviechecker.database.movie.MovieEntity;
import moviechecker.di.Favorite;
import moviechecker.di.Movie;

import java.net.URI;
import java.util.Objects;

@Entity
@Table(name = "favorite")
@EntityListeners(FavoriteDataListener.class)
public class FavoriteEntity implements Favorite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "movie_id", unique = true, nullable = false, updatable = false)
	private MovieEntity movie;

	/**
	 * @deprecated Only for hibernate usage.
	 */
	@Deprecated
	public FavoriteEntity() {
		super();
	}
	
	public FavoriteEntity(MovieEntity movie) {
		this.movie = movie;
	}
	
	public Movie getMovie() {
		return movie;
	}

	@Override
	@Transient
	public String getTitle() {
		return movie.getTitle();
	}

	@Transient
	public URI getLink() {
		return movie.getSite().getAddress().resolve(movie.getPath());
	}

	@Override
	public String toString() {
		return "Favorite: " + movie.toString();
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
		FavoriteEntity other = (FavoriteEntity) obj;
		return Objects.equals(movie, other.movie);
	}
}
