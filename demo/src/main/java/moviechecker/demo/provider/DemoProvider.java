package moviechecker.demo.provider;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import moviechecker.database.State;
import moviechecker.datasource.provider.AbstractMovieProvider;
import moviechecker.datasource.provider.DataRecord;
import org.springframework.stereotype.Component;

/**
 * Example of how to extract data from a website.
 */
@Component
public class DemoProvider extends AbstractMovieProvider {

    private static final String SITE = "https://some.site";

    /**
     * Pattern for detecting a line with the following data: identifier, title, link to a movie.
     */
    private static final Pattern MOVIE_PATTERN = Pattern.compile("<a href=\"(?<href>/(?<pageId>.*)\\.html)\">(?<title>.*)</a>");

    /**
     * Pattern for detecting a line with the following data: season number, episode number, episode title, link to episode.
     */
    private static final Pattern EPISODE_PATTERN = Pattern.compile("<a href=\"(?<href>.*/(?<season>\\d+)/(?<episode>\\d+)\\.html)\">(?<title>.*)</a>");

    /**
     * Pattern for detecting a line with a release data.
     */
    private static final Pattern DATE_PATTERN = Pattern.compile("(?<date>Новая серия в|Сегодня|Вчера|\\d{1,2}-\\d{2}-\\d{4}),? (?<time>\\d{2}:\\d{2})");

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("d-MM-yyyy");

    /**
     * Prepared data.html file is used as a page received from a site.
     *
     * @return The new reader.
     */
    private BufferedReader createHtmlReader() {
        return new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream("/data.html")), StandardCharsets.UTF_8));
    }

    @Override
    public void retrieveData() throws Exception {
        URI address = URI.create(SITE);

        // Use Builder to fill data.
        DataRecord.Builder dataRecordBuilder = new DataRecord.Builder();
        dataRecordBuilder.site(address);

        try (BufferedReader reader = createHtmlReader()) {
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {

                Matcher movieMatcher = MOVIE_PATTERN.matcher(inputLine);
                if (movieMatcher.find()) {
                    dataRecordBuilder.moviePageId(movieMatcher.group("pageId"))
                            .movieTitle(movieMatcher.group("title"))
                            .moviePath(movieMatcher.group("href"));

                    Matcher episodeMatcher = EPISODE_PATTERN.matcher(reader.readLine());
                    if (episodeMatcher.find()) {
                        dataRecordBuilder.seasonNumber(Integer.parseInt(episodeMatcher.group("season")))
                                .episodeNumber(Integer.parseInt(episodeMatcher.group("episode")))
                                .episodeTitle(episodeMatcher.group("title"))
                                .episodePath(episodeMatcher.group("href"));
                    } else {
                        throw new Exception("Unable to parse data for episode.");
                    }

                    Matcher dateMatcher = DATE_PATTERN.matcher(reader.readLine());
                    if (dateMatcher.find()) {
                        LocalDate localDate;
                        String date = dateMatcher.group("date");
                        switch (date) {
                            case "Новая серия в" -> {
                                dataRecordBuilder.episodeState(State.EXPECTED);
                                localDate = LocalDate.now();
                            }
                            case "Сегодня" -> {
                                dataRecordBuilder.episodeState(State.RELEASED);
                                localDate = LocalDate.now();
                            }
                            case "Вчера" -> {
                                dataRecordBuilder.episodeState(State.RELEASED);
                                localDate = LocalDate.now().minusDays(1);
                            }
                            default -> {
                                dataRecordBuilder.episodeState(State.RELEASED);
                                localDate = LocalDate.parse(date, dateFormat);
                            }
                        }
                        String time = dateMatcher.group("time");
                        if (time != null) {
                            dataRecordBuilder.episodeDate(LocalDateTime.of(localDate, LocalTime.parse(time)));
                        }

                        // Send data to database
                        saveRecord(dataRecordBuilder.build());
                    } else {
                        throw new Exception("Unable to parse date.");
                    }
                }
            }
        }
    }

}
