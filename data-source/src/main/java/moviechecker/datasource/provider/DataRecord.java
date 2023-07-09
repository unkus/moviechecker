package moviechecker.datasource.provider;

import moviechecker.database.State;

import java.net.URI;
import java.time.LocalDateTime;

public record DataRecord(URI siteAddress,
                         String moviePageId,
                         String movieTitle,
                         URI movieLink,
                         URI moviePosterLink,
                         Integer seasonNumber,
                         URI seasonLink,
                         Integer episodeNumber,
                         String episodeTitle,
                         URI episodeLink,
                         State episodeState,
                         LocalDateTime episodeDate) {

    public DataRecord(Builder builder) {
        this(builder.siteAddress,
                builder.moviePageId,
                builder.movieTitle,
                builder.movieLink,
                builder.moviePosterLink,
                builder.seasonNumber,
                builder.seasonLink,
                builder.episodeNumber,
                builder.episodeTitle,
                builder.episodeLink,
                builder.episodeState,
                builder.episodeDate);
    }

    public static class Builder {
        private URI siteAddress;
        private String moviePageId;
        private String movieTitle;
        private URI movieLink;
        private URI moviePosterLink;
        private Integer seasonNumber;
        private URI seasonLink;
        private Integer episodeNumber;
        private String episodeTitle;
        private URI episodeLink;
        private State episodeState;
        private LocalDateTime episodeDate;

        public Builder site(URI site) {
            this.siteAddress = site;
            return this;
        }

        public Builder moviePageId(String moviePageId) {
            this.moviePageId = moviePageId;
            return this;
        }

        public Builder movieTitle(String movieTitle) {
            this.movieTitle = movieTitle;
            return this;
        }

        public Builder movieLink(URI movieLink) {
            this.movieLink = movieLink;
            return this;
        }

        public Builder moviePosterLink(URI posterLink) {
            this.moviePosterLink = posterLink;
            return this;
        }

        public Builder seasonNumber(int seasonNumber) {
            this.seasonNumber = seasonNumber;
            return this;
        }

        public Builder seasonLink(URI seasonLink) {
            this.seasonLink = seasonLink;
            return this;
        }

        public Builder episodeNumber(int episodeNumber) {
            this.episodeNumber = episodeNumber;
            return this;
        }

        public Builder episodeTitle(String episodeTitle) {
            this.episodeTitle = episodeTitle;
            return this;
        }

        public Builder episodeLink(URI episodeLink) {
            this.episodeLink = episodeLink;
            return this;
        }

        public Builder episodeState(State episodeState) {
            this.episodeState = episodeState;
            return this;
        }

        public Builder episodeDate(LocalDateTime episodeDate) {
            this.episodeDate = episodeDate;
            return this;
        }

        private void validate() throws Exception {
            if (siteAddress == null) {
                throw new Exception("Site address is not set");
            }
            if (moviePageId == null) {
                throw new Exception("Movie page id is not set");
            }
            if (movieTitle == null) {
                throw new Exception("Movie title is not set");
            }
            if (movieLink == null) {
                throw new Exception("Movie link is not set");
            }
            if (moviePosterLink == null) {
                throw new Exception("Movie poster link is not set");
            }
            if (seasonNumber == null) {
                throw new Exception("Season number is not set");
            }
            if (seasonLink == null) {
                throw new Exception("Season link is not set");
            }
            if (episodeNumber == null) {
                throw new Exception("Episode number is not set");
            }
            if (episodeTitle == null) {
                throw new Exception("Episode title is not set");
            }
            if (episodeLink == null) {
                throw new Exception("Episode link is not set");
            }
            if (episodeState == null) {
                throw new Exception("State is not set");
            }
            if (episodeDate == null) {
                throw new Exception("Date is not set");
            }
        }

        public DataRecord build() throws Exception {
            validate();
            return new DataRecord(this);
        }
    }
}
