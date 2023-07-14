package moviechecker.database.season;

import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;
import moviechecker.database.episode.EpisodeEntity;
import moviechecker.database.movie.MovieEntity;
import moviechecker.di.Season;

@Entity
@Table(name = "season", uniqueConstraints = @UniqueConstraint(columnNames = { "movie_id", "number"}))
public class SeasonEntity implements Season {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "movie_id", referencedColumnName = "id", nullable = false, updatable = false)
	private MovieEntity movie;

	@Column(nullable = false, updatable = false)
	private int number;

	@Column(nullable = false)
	private String path;

	@OneToMany(mappedBy = "season", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<EpisodeEntity> episodes = new HashSet<>();
	
	/**
	 * @deprecated Only for hibernate usage.
	 */
	@Deprecated
	public SeasonEntity() {
		super();
	}
	
	public SeasonEntity(MovieEntity movie, int number) {
		this.movie = movie;
		this.number = number;
	}

	public MovieEntity getMovie() {
		return movie;
	}

	public int getNumber() {
		return number;
	}

	@Transient
	public String getTitle() {
		return movie.getTitle() + (number > 1 ? " " + number : "");
	}

	public String getPath() { return path; }

	public void setPath(String path) { this.path = path; }

	@Transient
	public URI getLink() {
		return movie.getSite().getAddress().resolve(path);
	}

	public Set<EpisodeEntity> getEpisodes() {
		return episodes;
	}

	@Override
	public String toString() {
		return movie.toString() + ": Сезон " + number;
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
		SeasonEntity other = (SeasonEntity) obj;
		return Objects.equals(movie, other.movie) && number == other.number;
	}
	
}
