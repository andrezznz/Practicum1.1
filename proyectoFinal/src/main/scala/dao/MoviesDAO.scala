package dao
import doobie._
import doobie.implicits._
import cats.effect.IO
import cats.implicits._
import modelos._
import config.Database

object MoviesDAO {

  def insertCol(collections: BelongsToCollection): ConnectionIO[Int] = {
    sql"""
     INSERT INTO collections (id, name, poster_path, backdrop_path)
     VALUES (
       ${collections.id},
       ${collections.name},
       ${collections.poster_path},
       ${collections.backdrop_path}
     )
   """.update.run
  }

  def insertCollections(collections: List[BelongsToCollection]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      collections.traverse(t => insertCol(t).transact(xa))
    }
  }

  def insertGen(genres: Genre): ConnectionIO[Int] = {
    sql"""
     INSERT INTO genres (id, name)
     VALUES (
       ${genres.id},
       ${genres.name}
     )
   """.update.run
  }

  def insertGenres(genres: List[Genre]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      genres.traverse(t => insertGen(t).transact(xa))
    }
  }

  def insertCom(companies: ProductionCompany): ConnectionIO[Int] = {
    sql"""
     INSERT INTO companies (name, id)
     VALUES (
       ${companies.name},
       ${companies.id}
     )
   """.update.run
  }

  def insertCompanies(companies: List[ProductionCompany]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      companies.traverse(t => insertCom(t).transact(xa))
    }
  }

  def insertCou(countries: ProductionCountry): ConnectionIO[Int] = {
    sql"""
     INSERT INTO countries (iso_3166_1, name)
     VALUES (
       ${countries.iso_3166_1},
       ${countries.name}
     )
   """.update.run
  }

  def insertCountries(countries: List[ProductionCountry]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      countries.traverse(t => insertCou(t).transact(xa))
    }
  }

  def insertLan(languages: SpokenLanguage): ConnectionIO[Int] = {
    sql"""
     INSERT INTO languages (iso_639_1, name)
     VALUES (
       ${languages.iso_639_1},
       ${languages.name}
     )
   """.update.run
  }

  def insertLanguages(languages: List[SpokenLanguage]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      languages.traverse(t => insertLan(t).transact(xa))
    }
  }

  def insertKey(keywords: Keyword): ConnectionIO[Int] = {
    sql"""
     INSERT INTO keywords (id, name)
     VALUES (
       ${keywords.id},
       ${keywords.name}
     )
   """.update.run
  }

  def insertKeywords(keywords: List[Keyword]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      keywords.traverse(t => insertKey(t).transact(xa))
    }
  }

  def insertCas(casts: Cast): ConnectionIO[Int] = {
    sql"""
     INSERT INTO casts (cast_id, characte, credit_id, gender, id, name, orde, profile_path)
     VALUES (
       ${casts.cast_id},
       ${casts.character},
       ${casts.credit_id},
       ${casts.gender},
       ${casts.id},
       ${casts.name},
       ${casts.order},
       ${casts.profile_path}
     )
   """.update.run
  }

  def insertCasts(casts: List[Cast]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      casts.traverse(t => insertCas(t).transact(xa))
    }
  }

  def insertCr(crew: Crew): ConnectionIO[Int] = {
    sql"""
     INSERT INTO crew (credit_id, department, gender, id, job, name, profile_path)
     VALUES (
       ${crew.credit_id},
       ${crew.department},
       ${crew.gender},
       ${crew.id},
       ${crew.job},
       ${crew.name},
       ${crew.profile_path}
     )
   """.update.run
  }

  def insertCrew(crews: List[Crew]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      crews.traverse(t => insertCr(t).transact(xa))
    }
  }

  def insertRat(ratings: Ratings): ConnectionIO[Int] = {
    sql"""
     INSERT INTO ratings (userId, rating, timestamp)
     VALUES (
       ${ratings.userId},
       ${ratings.rating},
       ${ratings.timestamp}
     )
   """.update.run
  }

  def insertRatings(ratings: List[Ratings]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      ratings.traverse(t => insertRat(t).transact(xa))
    }
  }

  def insertPel(peliculas: Pelicula): ConnectionIO[Int] = {
    sql"""
     INSERT INTO peliculas (adult, budget, homepage, id, imdb_id, original_language, original_title, overview, popularity, poster_path, release_date, revenue, runtime, status, tagline, title, video, vote_average, vote_count)
     VALUES (
       ${peliculas.adult},
       ${peliculas.budget},
       ${peliculas.homepage},
       ${peliculas.id},
       ${peliculas.imdb_id},
       ${peliculas.original_language},
       ${peliculas.original_title},
       ${peliculas.overview},
       ${peliculas.popularity},
       ${peliculas.poster_path},
       ${peliculas.release_date},
       ${peliculas.revenue},
       ${peliculas.runtime},
       ${peliculas.status},
       ${peliculas.tagline},
       ${peliculas.title},
       ${peliculas.video},
       ${peliculas.vote_average},
       ${peliculas.vote_count}
     )
   """.update.run
  }

  def insertPeliculas(peliculas: List[Pelicula]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      peliculas.traverse(t => insertPel(t).transact(xa))
    }
  }

}
