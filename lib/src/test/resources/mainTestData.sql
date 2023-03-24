-- 'https://site.one'
--
-- 'movie_one', 'Фильм первый', '/movie_one.html'
-- 1, '1', '/1.html'
-- 11, 'Серия 11', '/movie_one/1/11.html', 2, TIMESTAMP '2023-01-05 12:35:29'
-- 12, 'Серия 12', '/movie_one/1/12.html', 0, TIMESTAMP '2023-01-10 11:21:29'
--
-- 'movie_two', 'Фильм второй', '/movie_two.html'
-- 2, '2', '/2.html'
-- 24, 'Серия 24', '/movie_two/2/24.html', 2, TIMESTAMP '2023-01-08 12:35:29'
-- 25, 'Серия 25', '/movie_two/2/25.html', 1, TIMESTAMP '2023-01-09 13:23:16'
--
-- favorites:
-- 'movie_one', 11
-- 'movie_two', 24
--
-- 'https://site.two'
--
-- 'movie_one', 'Фильм первый', '/movie_one.html'
-- 1, '1', '/1.html'
-- 11, 'Серия 11', '/movie_one/1/11.html', 2, TIMESTAMP '2023-01-06 13:35:29'
-- 12, 'Серия 12', '/movie_one/1/12.html', 0, TIMESTAMP '2023-01-11 12:21:29'
--
-- 'movie_three', 'Фильм третий', '/movie_three.html'
-- 1, '1', '/1.html'
-- 1, 'Серия 1', '/movie_three/1/1.html', 1, TIMESTAMP '2023-01-06 18:33:41'
-- 2, 'Серия 2', '/movie_three/1/2.html', 0'
--
-- favorites:
-- 'movie_three'

-- site one
INSERT INTO	site (address) VALUES ('https://site.one');

-- movie one
INSERT INTO movie (site_id, page_id, title, link)
    SELECT s.id, 'movie_one', 'Фильм первый', '/movie_one.html'
    FROM site s
    WHERE s.address = 'https://site.one';

-- season 1
INSERT INTO season (movie_id, number, title, link)
    SELECT m.id, 1, '1', '/1.html'
    FROM movie m
    JOIN site s ON s.id = m.site_id AND s.address = 'https://site.one'
    WHERE m.page_id = 'movie_one';

-- episodes
INSERT INTO episode (season_id, number, title, link, state, release_date)
    SELECT s.id, tmp.number, tmp.title, tmp.link, tmp.state, tmp.release_date
    FROM ( VALUES
            (11, 'Серия 11', '/movie_one/1/11.html', 2, TIMESTAMP '2023-01-05 12:35:29'),
            (12, 'Серия 12', '/movie_one/1/12.html', 0, TIMESTAMP '2023-01-10 11:21:29')
        ) tmp (number, title, link, state, release_date)
    JOIN season s ON s.number = 1
    JOIN movie m ON m.id = s.movie_id AND m.page_id = 'movie_one'
    JOIN site ON site.id = m.site_id AND site.address = 'https://site.one';

-- movie two
INSERT INTO movie (site_id, page_id, title, link)
    SELECT s.id, 'movie_two', 'Фильм второй', '/movie_two.html'
    FROM site s
    WHERE s.address = 'https://site.one';

-- season 2
INSERT INTO season (movie_id, number, title, link)
    SELECT m.id, 2, '2', '/2.html'
    FROM movie m
    JOIN site s ON s.id = m.site_id AND s.address = 'https://site.one'
    WHERE m.page_id = 'movie_two';

-- episodes
INSERT INTO episode (season_id, number, title, link, state, release_date)
    SELECT s.id, tmp.number, tmp.title, tmp.link, tmp.state, tmp.release_date
    FROM
        ( VALUES
            (24, 'Серия 24', '/movie_two/2/24.html', 2, TIMESTAMP '2023-01-08 12:35:29'),
            (25, 'Серия 25', '/movie_two/2/25.html', 1, TIMESTAMP '2023-01-09 13:23:16')
        ) tmp (number, title, link, state, release_date)
    JOIN season s ON s.number = 2
    JOIN movie m ON m.id = s.movie_id AND m.page_id = 'movie_two'
    JOIN site ON site.id = m.site_id AND site.address = 'https://site.one';

-- favorites
INSERT INTO favorite (movie_id, episode_id)
    SELECT m.id, e.id
    FROM episode e
    JOIN season s ON s.id = e.season_id AND s.number = 1
    JOIN movie m ON m.id = s.movie_id AND m.page_id = 'movie_one'
    JOIN site ON site.id = m.site_id AND site.address = 'https://site.one'
    WHERE e.number = 11;

INSERT INTO favorite (movie_id, episode_id)
    SELECT m.id, e.id
    FROM episode e
    JOIN season s ON s.id = e.season_id AND s.number = 2
    JOIN movie m ON m.id = s.movie_id AND m.page_id = 'movie_two'
    JOIN site ON site.id = m.site_id AND site.address = 'https://site.one'
    WHERE e.number = 24;

-- site two
INSERT INTO	site (address) VALUES ('https://site.two');

-- movie one
INSERT INTO movie (site_id, page_id, title, link)
    SELECT s.id, 'movie_one', 'Фильм первый', '/movie_one.html'
    FROM site s
    WHERE s.address = 'https://site.two';

-- season 1
INSERT INTO season (movie_id, number, title, link)
    SELECT m.id, 1, '1', '/1.html'
    FROM movie m
    JOIN site s ON s.id = m.site_id AND s.address = 'https://site.two'
    WHERE m.page_id = 'movie_one';

-- episodes
INSERT INTO episode (season_id, number, title, link, state, release_date)
    SELECT s.id, tmp.number, tmp.title, tmp.link, tmp.state, tmp.release_date
    FROM ( VALUES
            (11, 'Серия 11', '/movie_one/1/11.html', 2, TIMESTAMP '2023-01-06 13:35:29'),
            (12, 'Серия 12', '/movie_one/1/12.html', 0, TIMESTAMP '2023-01-11 12:21:29')
        ) tmp (number, title, link, state, release_date)
    JOIN season s ON s.number = 1
    JOIN movie m ON m.id = s.movie_id AND m.page_id = 'movie_one'
    JOIN site ON site.id = m.site_id AND site.address = 'https://site.two';

-- movie three
INSERT INTO movie (site_id, page_id, title, link)
    SELECT s.id, 'movie_three', 'Фильм третий', '/movie_three.html'
    FROM site s
    WHERE s.address = 'https://site.two';

-- season 1
INSERT INTO season (movie_id, number, title, link)
    SELECT m.id, 1, '1', '/1.html'
    FROM movie m
    JOIN site s ON s.id = m.site_id AND s.address = 'https://site.two'
    WHERE m.page_id = 'movie_three';

-- episodes
INSERT INTO episode (season_id, number, title, link, state, release_date)
    SELECT s.id, tmp.number, tmp.title, tmp.link, tmp.state, tmp.release_date
    FROM
        ( VALUES
            (1, 'Серия 1', '/movie_three/1/1.html', 1, TIMESTAMP '2023-01-06 18:33:41'),
            (2, 'Серия 2', '/movie_three/1/2.html', 0, NULL)
        ) tmp (number, title, link, state, release_date)
    JOIN season s ON s.number = 1
    JOIN movie m ON m.id = s.movie_id AND m.page_id = 'movie_three'
    JOIN site ON site.id = m.site_id AND site.address = 'https://site.two';

-- favorites
INSERT INTO favorite (movie_id)
    SELECT m.id
    FROM movie m
    JOIN site ON site.id = m.site_id AND site.address = 'https://site.two'
    WHERE m.page_id = 'movie_one';
