package moviechecker.database.movie;

import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import moviechecker.database.season.Season;
import moviechecker.database.site.Site;
import moviechecker.database.converters.UriPersistanceConverter;

@Entity
@Table(name = "movie", uniqueConstraints = @UniqueConstraint(columnNames = { "site_id", "page_id" }))
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "site_id", referencedColumnName = "id", nullable = false, updatable = false)
	private Site site;

	@Column(name = "page_id", nullable = false, updatable = false)
	private String pageId;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	@Convert(converter = UriPersistanceConverter.class)
	private URI link;
	
	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Season> seasons = new HashSet<>();
	
	/**
	 * @deprecated Only for hibernate usage.
	 */
	@Deprecated
	public Movie() {
		super();
	}
	
	public Movie(Site site, String pageId) {
		this.site = site;
		this.pageId = pageId;
	}
	
	public Site getSite() {
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

	public URI getLink() {
		return link;
	}

	public void setLink(URI link) {
		this.link = link;
	}

	public Set<Season> getSeasons() {
		return seasons;
	}

	@Override
	public String toString() {
		return site + " " + title;
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
		Movie other = (Movie) obj;
		return Objects.equals(site, other.site) && Objects.equals(pageId, other.pageId);
	}

}
