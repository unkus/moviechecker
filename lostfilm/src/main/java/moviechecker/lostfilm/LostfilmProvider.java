package moviechecker.lostfilm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import moviechecker.core.di.State;
import moviechecker.datasource.di.DataRecord;
import moviechecker.datasource.di.DataRecordProvider;
import org.springframework.stereotype.Component;

@Component
public class LostfilmProvider implements DataRecordProvider {

    private static final Pattern NEW_MOVIE_CLASS_PATTERN = Pattern.compile("<a class=\"new-movie\" href=\"(?<href>.+)/\" title=\"(?<title>.+)\">");
    private static final Pattern TODAY_CLASS_PATTERN = Pattern.compile("<td class=\"today\">");
    private static final Pattern EXPECTED_EPISODE_PATTERN = Pattern.compile("<a href=\"(?<href>.+)/\" class=\"title\">(?<title>.+)</br>");
    private static final Pattern BREADCRUMBS_CLASS_PATTERN = Pattern.compile("<div class=\"breadcrumbs-pane\">");
    private static final Pattern MOVIE_ITEM_CLASS_PATTERN = Pattern.compile("<a href=\"(?<moviePath>/series/(?<moviePageId>.+))/\" class=\"item\">(?<movieTitle>.+)</a>");
    private static final Pattern SEASON_ITEM_CLASS_PATTERN = Pattern.compile("<a href=\"(?<seasonRef>.+)/\" class=\"item\"><div class=\"arrow\"></div>(?<seasonNumber>\\d+) сезон</a>");
    private static final Pattern EPISODE_ITEM_CLASS_PATTERN = Pattern.compile("<a href=\"(?<episodeRef>.+)/\" class=\"item\"><div class=\"arrow\"></div>(?<episodeNumber>\\d+) серия</a>");
    private static final Pattern SERIA_HEADER_CLASS_PATTERN = Pattern.compile("<div class=\"seria-header\">");
    private static final Pattern THUMBS_CLASS_PATTERN = Pattern.compile("<img src=\"(?<posterRef>.+)\" class=\"thumb\" />");
    private static final Pattern TITLE_RU_CLASS_PATTERN = Pattern.compile("<h1 class=\"title-ru\">(?<episodeTitle>.+)</h1>");
    private static final Pattern TITLE_EN_CLASS_PATTERN = Pattern.compile("<div class=\"title-en\">(?<episodeTitle>.+)</div>");
    private static final Pattern EXPECTED_CLASS_PATTERN = Pattern.compile("<div class=\"expected\">Ожидается (?<date>.+)</div>");
    private static final Pattern DATE_PATTERN = Pattern.compile("<span data-proper=\".+\" data-released=\"(?<dateReleased>\\d{2} .+ \\d{4})\">.+</span>");

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    private static final String SITE_ADDRESS = "https://www.lostfilmtv5.site";

    private BufferedReader createHtmlReader(URL url) throws IOException {
        return new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
    }

    @Override
    public Collection<DataRecord> retrieveData() throws Exception {
        URI siteUri = URI.create(SITE_ADDRESS);

        LinkedHashMap<String, DataRecord> recordList = new LinkedHashMap<>();

        try (BufferedReader reader = createHtmlReader(siteUri.resolve("/schedule").toURL())) {
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                String href = null;
                Matcher newMovieClassMatcher = NEW_MOVIE_CLASS_PATTERN.matcher(inputLine);
                if (newMovieClassMatcher.find()) {
                    href = newMovieClassMatcher.group("href");
                } else if (TODAY_CLASS_PATTERN.matcher(inputLine).find()) {
                    reader.readLine(); // skip tag

                    Matcher entryMatcher = EXPECTED_EPISODE_PATTERN.matcher(reader.readLine());
                    if (entryMatcher.find()) {
                        href = entryMatcher.group("href");
                    }
                }

                if (href != null) {
                    if (href.matches(".+episode_\\d+")) {
                        // episode
                        recordList.put(href, getEpisodeDetails(siteUri.resolve(href).toURL()));
                    } else {
                        // TODO: process movie
                    }
                }
            }
        }
        return recordList.values();
    }

    private DataRecord getEpisodeDetails(URL url) throws Exception {
        DataRecord.Builder dataRecordBuilder = new DataRecord.Builder();
        dataRecordBuilder.site(SITE_ADDRESS);
        dataRecordBuilder.episodeState(State.RELEASED);

        try (BufferedReader reader = createHtmlReader(url)) {
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                Matcher breadcrumbsMatcher = BREADCRUMBS_CLASS_PATTERN.matcher(inputLine);
                if (breadcrumbsMatcher.find()) {
                    Matcher movieMatcher = MOVIE_ITEM_CLASS_PATTERN.matcher(reader.readLine());
                    movieMatcher.find();
                    dataRecordBuilder.moviePageId(movieMatcher.group("moviePageId"))
                            .moviePath(movieMatcher.group("moviePath"))
                            .movieTitle(movieMatcher.group("movieTitle"));

                    reader.readLine(); // skip

                    Matcher seasonMatcher = SEASON_ITEM_CLASS_PATTERN.matcher(reader.readLine());
                    seasonMatcher.find();
                    dataRecordBuilder.seasonPath(seasonMatcher.group("seasonRef"))
                            .seasonNumber(Integer.parseInt(seasonMatcher.group("seasonNumber")));

                    Matcher episodeMatcher = EPISODE_ITEM_CLASS_PATTERN.matcher(reader.readLine());
                    episodeMatcher.find();
                    dataRecordBuilder.episodePath(episodeMatcher.group("episodeRef"))
                            .episodeNumber(Integer.parseInt(episodeMatcher.group("episodeNumber")));
                } else if (SERIA_HEADER_CLASS_PATTERN.matcher(inputLine).find()) {
                    Matcher posterMatcher = THUMBS_CLASS_PATTERN.matcher(reader.readLine());
                    posterMatcher.find();
                    dataRecordBuilder.moviePosterLink(posterMatcher.group("posterRef"));

                    Matcher ruEpisodeTitleMatcher = TITLE_RU_CLASS_PATTERN.matcher(reader.readLine());
                    ruEpisodeTitleMatcher.find();
                    dataRecordBuilder.episodeTitle(ruEpisodeTitleMatcher.group("episodeTitle"));

                    Matcher enEpisodeTitleMatcher = TITLE_EN_CLASS_PATTERN.matcher(reader.readLine());
                    enEpisodeTitleMatcher.find();
                    //
                } else {
                    Matcher expectedMatcher = EXPECTED_CLASS_PATTERN.matcher(inputLine);
                    if (expectedMatcher.find()) {
                        dataRecordBuilder.episodeState(State.EXPECTED);
                    }
                    Matcher dateMatcher = DATE_PATTERN.matcher(inputLine);
                    if (dateMatcher.find()) {
                        dataRecordBuilder.episodeDate(LocalDateTime.of(LocalDate.parse(dateMatcher.group("dateReleased"), dateFormat), LocalTime.MIN));
                        return dataRecordBuilder.build();
                    }
                }
            }
        }

        throw new Exception("No record produced for " + url);
    }
}
