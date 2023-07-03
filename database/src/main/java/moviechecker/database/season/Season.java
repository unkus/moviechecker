package moviechecker.database.season;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;
import moviechecker.database.episode.Episode;
import moviechecker.database.movie.Movie;

@Entity
@Table(name = "season", uniqueConstraints = @UniqueConstraint(columnNames = { "movie_id", "number"}))
public class Season {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "movie_id", referencedColumnName = "id", nullable = false, updatable = false)
	private Movie movie;

	@Column(nullable = false, updatable = false)
	private int number;

	@OneToMany(mappedBy = "season", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Episode> episodes = new HashSet<>();
	
	/**
	 * @deprecated Only for hibernate usage.
	 */
	@Deprecated
	public Season() {
		super();
	}
	
	public Season(Movie movie, int number) {
		this.movie = movie;
		this.number = number;
	}

	public Movie getMovie() {
		return movie;
	}

	public int getNumber() {
		return number;
	}

	public String getTitle() {
		return movie.getTitle() + " " + number;
	}

	public Set<Episode> getEpisodes() {
		return episodes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(movie, number);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Season other = (Season) obj;
		return Objects.equals(movie, other.movie) && number == other.number;
	}

	@Override
	public String toString() {
		return movie + " Сезон " + number;
	}
	
}
