package moviechecker.lostfilm;

import moviechecker.database.State;
import moviechecker.datasource.provider.AbstractMovieProvider;
import moviechecker.datasource.provider.DataRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class LostfilmProvider extends AbstractMovieProvider {

    private static final String SITE = "https://www.lostfilmtv5.site";

    private static final Pattern NEW_MOVIE_TAG_PATTERN = Pattern
            .compile("<a class=\"new-movie\" href=\"(?<moviePath>/series/(?<moviePageId>.+))(?<seasonPath>/.+)(?<episodePath>/.+)/\" title=\"(?<title>.+)\">");
    private static final Pattern EPISODE_TITLE_TAG_PATTERN = Pattern.compile("<div class=\"title\">");
    private static final Pattern DATE_TAG_PATTERN = Pattern.compile("<div class=\"date\">(?<date>.+)</div>");
    private static final Pattern IMG_TAG_PATTERN = Pattern.compile("<img src=\"(?<poster>.+)\" />");
    private static final Pattern ID_PATTERN = Pattern.compile("(?<seasonNumber>\\d+) сезон (?<episodeTitle>(?<episodeNumber>\\d+) серия)");

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private BufferedReader createHtmlReader() throws IOException {
        URL url = new URL(SITE);
        return new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
    }

    @Override
    public void retrieveData() throws Exception {
        URI address = URI.create(SITE);

        DataRecord.Builder dataRecordBuilder = new DataRecord.Builder();
        dataRecordBuilder.site(address);
        dataRecordBuilder.episodeState(State.RELEASED);

        try (BufferedReader reader = createHtmlReader()) {
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                Matcher movieMatcher = NEW_MOVIE_TAG_PATTERN.matcher(inputLine);
                if (movieMatcher.find()) {
                    dataRecordBuilder
                            .moviePageId(movieMatcher.group("moviePageId"))
                            .movieTitle(movieMatcher.group("title"))
                            .movieLink(URI.create(movieMatcher.group("moviePath")))
                            .seasonLink(URI.create(movieMatcher.group("seasonPath")))
                            .episodeLink(URI.create(movieMatcher.group("episodePath")));
                } else {
                    continue;
                }

                Matcher titleMatcher = EPISODE_TITLE_TAG_PATTERN.matcher(reader.readLine());
                if (titleMatcher.find()) {
                    String line = reader.readLine().trim();
                    Matcher idMatcher = ID_PATTERN.matcher(line);
                    if(idMatcher.find()) {
                        dataRecordBuilder
                                .seasonNumber(Integer.parseInt(idMatcher.group("seasonNumber")))
                                .episodeNumber(Integer.parseInt(idMatcher.group("episodeNumber")))
                                .episodeTitle(idMatcher.group("episodeTitle"));
                        reader.readLine(); // skip closing tag line
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }

                Matcher dateMatcher = DATE_TAG_PATTERN.matcher(reader.readLine());
                if (dateMatcher.find()) {
                    String dateString = dateMatcher.group("date");
                    LocalDateTime date = LocalDateTime.of(LocalDate.parse(dateString, dateFormat), LocalTime.MIN);
                    dataRecordBuilder.episodeDate(date);
                } else {
                    continue;
                }

                Matcher posterMatcher = IMG_TAG_PATTERN.matcher(reader.readLine());
                if (posterMatcher.find()) {
                    dataRecordBuilder.moviePosterLink(URI.create(posterMatcher.group("posterPath")));
                } else {
                    continue;
                }

                saveRecord(dataRecordBuilder.build());
            }
        }
    }
}