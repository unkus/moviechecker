package moviechecker.database.episode;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import jakarta.persistence.*;
import moviechecker.database.movie.Movie;
import moviechecker.database.season.Season;
import moviechecker.database.State;

@Entity
@Table(name = "episode", uniqueConstraints = @UniqueConstraint(columnNames = { "season_id", "number" }))
public class Episode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "season_id", referencedColumnName = "id", nullable = false, updatable = false)
	private Season season;

	@Column(nullable = false, updatable = false)
	private int number;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String path;
	
	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private State state = State.EXPECTED;

	@Column(name = "date", columnDefinition = "TIMESTAMP")
	private LocalDateTime date;

	/**
	 * @deprecated Only for hibernate usage.
	 */
	@Deprecated
	public Episode() {
		super();
	}
	
	public Episode(Season season, int number) {
		this.season = season;
		this.number = number;
	}

	@Transient
	public boolean isNew() {
		return id == 0;
	}

	public Season getSeason() {
		return season;
	}

	public Movie getMovie() {
		return season.getMovie();
	}
	
	public int getNumber() {
		return number;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Optional<LocalDateTime> getDate() {
		return Optional.ofNullable(date);
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return season + " " + title + " " + date;
	}

	@Override
	public int hashCode() {
		return Objects.hash(season, number);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Episode other = (Episode) obj;
		return Objects.equals(season, other.season) && number == other.number;
	}
	
}