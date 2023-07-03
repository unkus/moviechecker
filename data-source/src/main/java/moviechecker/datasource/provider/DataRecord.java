package moviechecker.datasource.provider;

import moviechecker.database.State;

import java.net.URI;
import java.time.LocalDateTime;

public record DataRecord(URI siteAddress,
                         String moviePageId,
                         String movieTitle,
                         URI movieLink,
                         Integer seasonNumber,
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
                builder.seasonNumber,
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
        private Integer seasonNumber;
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

        public Builder seasonNumber(int seasonNumber) {
            this.seasonNumber = seasonNumber;
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

        public DataRecord build() {
            return new DataRecord(this);
        }
    }
}
