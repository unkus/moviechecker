# Movie Checker Library
Описание таблиц базы и поставщика данных.

## База данных
База включает 5 таблицы: site, movie, season, episode, favorite.
Отношения:
- один site ко многим movie
- один movie ко многим season
- один season ко многим episode
- один favorite к одному movie
- один favorite к одному episode

## Поставщик данных
Поставщик реализует получение данных от источника и зполняет базу данных а взаимодествие с GUI берет на себя AbstractMovieProvider.