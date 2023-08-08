package moviechecker.database.episode;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.*;
import moviechecker.database.season.SeasonEntity;
import moviechecker.core.di.Episode;
import moviechecker.core.di.State;

@Entity
@Table(name = "episode", uniqueConstraints = @UniqueConstraint(columnNames = { "season_id", "number" }))
@EntityListeners(EpisodeDataListener.class)
public class EpisodeEntity implements Episode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "season_id", referencedColumnName = "id", nullable = false, updatable = false)
	private SeasonEntity season;

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
	public EpisodeEntity() {
		super();
	}
	
	public EpisodeEntity(SeasonEntity season, int number) {
		this.season = season;
		this.number = number;
	}

	public SeasonEntity getSeason() {
		return season;
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

	public LocalDateTime getReleaseDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	@Transient
	public URI getLink() {
		return getSeason().getMovie().getSite().getAddress().resolve(path);
	}

	@Override
	public String toString() {
		return season.toString() + ": " + title;
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
		EpisodeEntity other = (EpisodeEntity) obj;
		return Objects.equals(season, other.season) && number == other.number;
	}
	
}