# Movie Checker
The application allows you to check for new episodes without visiting a website.  
[Project issues](https://github.com/users/unkus/projects/1)

## 1. Goals
The goal of the project is to gain experience with the next stack and patterns:
- Gradle
- Java 17
- Spring (Spring Boot, Spring Data JPA)
- H2 database
- Jupiter (JUnit5)
- SOLID
- MVC

## 2. Modules
Project has core, database, datasource and some number of modules to provide data.

### 2.1 Core
Here placed UI part (possibly will be extracted to separate module).Provides a couple of views representing data received from a provider. Provides the ability to open episodes in the default system browser and mark movies as favorites.

![Released view](./released.png)
![Expected view](./expected.png)
![Favorite view](./favorites.png)

#### 2.1.1 Core DI
This module encapsulate dependency inversion.

### 2.2 Database
The data is distributed across tables: site, movie, season, episode and favorite.

one-to-many relations: 
- site->movie->season->episode 

one-to-one relations: 
- favorite->movie 
- favorite->episode - keeping last viewed episode

### 2.3 Datasource
The idea is to provide an interface and basic functionality for data providers developed as a separate library.
Common functions allowing to a data provider to concentrate on getting data.
- Receiving data request and calling implemented interface.
- Sends data received event when data provider prepares it.
- Store data in database.
- Sends data error event in case of error during data retrieving.

#### 2.3.1 Datasource DI
This module encapsulate dependency inversion.
Describes behaviour for data provider.

### 2.4 Lostfilm
Demonstrates how data can be retrieved from website and transferred to the database.
Provider must be annotated with Component stereotype and implement provider interface.
