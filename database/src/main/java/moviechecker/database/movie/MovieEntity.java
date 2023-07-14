package moviechecker.database.movie;

import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;
import moviechecker.database.converters.UriPersistanceConverter;
import moviechecker.database.season.SeasonEntity;
import moviechecker.database.site.SiteEntity;
import moviechecker.di.Movie;

@Entity
@Table(name = "movie", uniqueConstraints = @UniqueConstraint(columnNames = { "site_id", "page_id" }))
public class MovieEntity implements Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "site_id", referencedColumnName = "id", nullable = false, updatable = false)
	private SiteEntity site;

	@Column(name = "page_id", nullable = false, updatable = false)
	private String pageId;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String path;

	@Column(name = "poster_link")
	@Convert(converter = UriPersistanceConverter.class)
	private URI posterLink;
	
	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<SeasonEntity> seasons = new HashSet<>();
	
	/**
	 * @deprecated Only for hibernate usage.
	 */
	@Deprecated
	public MovieEntity() {
		super();
	}
	
	public MovieEntity(SiteEntity site, String pageId) {
		this.site = site;
		this.pageId = pageId;
	}
	
	public SiteEntity getSite() {
		return site;
	}

	public String getPageId() {
		return pageId;
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

	public URI getPosterLink() { return posterLink; }

	public void setPosterLink(URI posterLink) { this.posterLink = posterLink; }

	public Set<SeasonEntity> getSeasons() {
		return seasons;
	}

	@Transient
	public URI getLink() {
		return site.getAddress().resolve(path);
	}

	@Override
	public String toString() {
		return site.toString() + ": " + title;
	}

	@Override
	public int hashCode() {
		return Objects.hash(site, pageId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MovieEntity other = (MovieEntity) obj;
		return Objects.equals(site, other.site) && Objects.equals(pageId, other.pageId);
	}

}
