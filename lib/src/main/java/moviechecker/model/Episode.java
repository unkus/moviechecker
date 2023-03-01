package moviechecker.model;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import moviechecker.converter.UriPersistanceConverter;

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
	private String episode_title;

	@Column(nullable = false)
	@Convert(converter = UriPersistanceConverter.class)
	private URI link;
	
	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private State state = State.EXPECTED;

	@Column(name = "release_date", columnDefinition = "TIMESTAMP")
	private LocalDateTime releaseDate;

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
		return episode_title;
	}

	public void setTitle(String title) {
		this.episode_title = title;
	}

	public URI getLink() {
		return link;
	}

	public void setLink(URI link) {
		this.link = link;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Optional<LocalDateTime> getReleaseDate() {
		return Optional.ofNullable(releaseDate);
	}

	public void setReleaseDate(LocalDateTime releaseDate) {
		this.releaseDate = releaseDate;
	}

	@Override
	public String toString() {
		return season + " " + episode_title + " " + releaseDate;
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