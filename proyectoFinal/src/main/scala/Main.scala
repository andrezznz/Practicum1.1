import cats.effect.{IO, IOApp}
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import play.api.libs.json._
import modelos._

import dao.MoviesDAO
import doobie._
import doobie.implicits._
import cats.effect.{IO, IOApp}
import cats.implicits._
import config.Database

object Main extends IOApp.Simple {

  // Definimos los formatos JSON por separado para cada tipo
  val belongsToCollectionFormat: OFormat[BelongsToCollection] = Json.format[BelongsToCollection]
  val genreFormat: OFormat[Genre] = Json.format[Genre]
  val productionCompanyFormat: OFormat[ProductionCompany] = Json.format[ProductionCompany]
  val productionCountryFormat: OFormat[ProductionCountry] = Json.format[ProductionCountry]
  val spokenLanguageFormat: OFormat[SpokenLanguage] = Json.format[SpokenLanguage]
  val keywordFormat: OFormat[Keyword] = Json.format[Keyword]
  val castFormat: OFormat[Cast] = Json.format[Cast]
  val crewFormat: OFormat[Crew] = Json.format[Crew]
  val ratingFormat: OFormat[Ratings] = Json.format[Ratings]

  // Función para limpiar y parsear JSON
  def cleanAndParseJson[T](jsonString: String)(implicit reads: Reads[T]): Option[List[T]] = {
    try {
      // Limpiar los espacios innecesarios
      val cleanedJson = jsonString
        .trim
        .replaceAll("'", "\"") // Comillas simples a dobles
        .replaceAll("None", "null") // Reemplazar None por null
        .replaceAll("True", "true") // Normalizar booleanos
        .replaceAll("False", "false")
        .replaceAll("""\\""", "") // Eliminar escapes innecesarios
        .replaceAll("""\s*\{\s*""", "{") // Eliminar espacios al principio de un objeto
        .replaceAll("""\s*\}\s*""", "}") // Eliminar espacios al final de un objeto
        .replaceAll("""\s*,\s*""", ",") // Eliminar espacios innecesarios entre los elementos de una lista o objeto
        .replaceAll("""\s*:\s*""", ":") // Eliminar espacios innecesarios alrededor de los dos puntos (en los objetos)
        .replaceAll("""\s+""", " ") // Reducir múltiples espacios a uno solo

      // Agregar corchetes si no están presentes
      val jsonWithBrackets = if (!(cleanedJson.startsWith("[") && cleanedJson.endsWith("]"))) {
        s"[$cleanedJson]"
      } else {
        cleanedJson
      }

      val json = Json.parse(jsonWithBrackets)
      json.asOpt[List[T]] // Intentar convertir al tipo T
    } catch {
      case _: Exception => None // Si falla, devolver None
    }
  }


  val pathToCsv = "src/main/resources/data/pi_movies_small.csv"
  val dataSource = new java.io.File(pathToCsv).readCsv[List, Movies](rfc.withHeader(true).withCellSeparator(';'))

  // Capturar casos correctos e incorrectos
  val movies_ok = dataSource.collect { case Right(movie) => movie }
  val movies_fail = dataSource.collect { case Left(error) => error }

  println(s"TOTAL DE FILAS: ${dataSource.size}")
  println(s"FILAS LEIDAS CORRECTAMENTE: ${movies_ok.size}")
  println(s"FILAS LEIDAS INCORRECTAMENTE: ${movies_fail.size}")

  // Eliminar duplicados
  val movies_no_duplicates = movies_ok.distinct.groupBy(_.id).map { case (_, movies) =>
    movies.head
  }.toList
  println(s"Filas después de eliminar duplicados: ${movies_no_duplicates.size}")

  // Crear listas separadas por tipo JSON
  val belongsToCollectionList = movies_no_duplicates.flatMap { movie =>
    cleanAndParseJson[BelongsToCollection](movie.belongs_to_collection)(belongsToCollectionFormat).getOrElse(List())
  }

  val genresList = movies_no_duplicates.flatMap { movie =>
    cleanAndParseJson[Genre](movie.genres)(genreFormat).getOrElse(List())
  }

  val productionCompaniesList = movies_no_duplicates.flatMap { movie =>
    cleanAndParseJson[ProductionCompany](movie.production_companies)(productionCompanyFormat).getOrElse(List())
  }

  val productionCountriesList = movies_no_duplicates.flatMap { movie =>
    cleanAndParseJson[ProductionCountry](movie.production_countries)(productionCountryFormat).getOrElse(List())
  }

  val spokenLanguagesList = movies_no_duplicates.flatMap { movie =>
    cleanAndParseJson[SpokenLanguage](movie.spoken_languages)(spokenLanguageFormat).getOrElse(List())
  }

  val keywordsList = movies_no_duplicates.flatMap { movie =>
    cleanAndParseJson[Keyword](movie.keywords)(keywordFormat).getOrElse(List())
  }

  val castList = movies_no_duplicates.flatMap { movie =>
    cleanAndParseJson[Cast](movie.cast)(castFormat).getOrElse(List())
  }

  val crewList = movies_no_duplicates.flatMap { movie =>
    cleanAndParseJson[Crew](movie.crew)(crewFormat).getOrElse(List())
  }

  val ratingsList = movies_no_duplicates.flatMap { movie =>
    cleanAndParseJson[Ratings](movie.ratings)(ratingFormat).getOrElse(List())
  }

  val peliculasList = movies_no_duplicates.map { movie =>
    Pelicula(
      adult = movie.adult,
      budget = movie.budget,
      homepage = movie.homepage,
      id = movie.id,
      imdb_id = movie.imdb_id,
      original_language = movie.original_language,
      original_title = movie.original_title,
      overview = movie.overview,
      popularity = movie.popularity,
      poster_path = movie.poster_path,
      release_date = movie.release_date,
      revenue = movie.revenue,
      runtime = movie.runtime,
      status = movie.status,
      tagline = movie.tagline,
      title = movie.title,
      video = movie.video,
      vote_average = movie.vote_average,
      vote_count = movie.vote_count
    )
  }

  // Mostrar los resultados
  println(s"Total de registros para belongsToCollection: ${belongsToCollectionList.size}")
  println(s"Total de registros para genres: ${genresList.size}")
  println(s"Total de registros para productionCompanies: ${productionCompaniesList.size}")
  println(s"Total de registros para productionCountries: ${productionCountriesList.size}")
  println(s"Total de registros para spokenLanguages: ${spokenLanguagesList.size}")
  println(s"Total de registros para keywords: ${keywordsList.size}")
  println(s"Total de registros para cast: ${castList.size}")
  println(s"Total de registros para crew: ${crewList.size}")
  println(s"Total de registros para ratings: ${ratingsList.size}")

  // Ejemplo de impresión de las primeras 5 entradas de cada lista
  println("Primeros 5 de belongsToCollection: " + belongsToCollectionList.take(5).mkString(", "))
  println("Primeros 5 de genres: " + genresList.take(5).mkString(", "))
  println("Primeros 5 de productionCompanies: " + productionCompaniesList.take(5).mkString(", "))
  println("Primeros 5 de productionCountries: " + productionCountriesList.take(5).mkString(", "))
  println("Primeros 5 de spokenLanguages: " + spokenLanguagesList.take(5).mkString(", "))
  println("Primeros 5 de keywords: " + keywordsList.take(5).mkString(", "))
  println("Primeros 5 de cast: " + castList.take(5).mkString(", "))
  println("Primeros 5 de crew: " + crewList.take(5).mkString(", "))
  println("Primeros 5 de ratings: " + ratingsList.take(5).mkString(", "))

  def run: IO[Unit] = for {
    resultCollections <- MoviesDAO.insertCollections(belongsToCollectionList) // Inserta datos y extrae resultado con <-
    _ <- IO.println(s"Registros insertados: ${resultCollections.size}") // Imprime cantidad
    resultGenres <- MoviesDAO.insertGenres(genresList) // Inserta datos y extrae resultado con <-
    _ <- IO.println(s"Registros insertados: ${resultGenres.size}") // Imprime cantidad
    resultCompanies <- MoviesDAO.insertCompanies(productionCompaniesList) // Inserta datos y extrae resultado con <-
    _ <- IO.println(s"Registros insertados: ${resultCompanies.size}") // Imprime cantidad
    resultCountries <- MoviesDAO.insertCountries(productionCountriesList) // Inserta datos y extrae resultado con <-
    _ <- IO.println(s"Registros insertados: ${resultCountries.size}") // Imprime cantidad
    resultLanguages <- MoviesDAO.insertLanguages(spokenLanguagesList) // Inserta datos y extrae resultado con <-
    _ <- IO.println(s"Registros insertados: ${resultLanguages.size}") // Imprime cantidad
    resultKeywords <- MoviesDAO.insertKeywords(keywordsList) // Inserta datos y extrae resultado con <-
    _ <- IO.println(s"Registros insertados: ${resultKeywords.size}") // Imprime cantidad
    resultCasts <- MoviesDAO.insertCasts(castList) // Inserta datos y extrae resultado con <-
    _ <- IO.println(s"Registros insertados: ${resultCasts.size}") // Imprime cantidad
    resultCrew <- MoviesDAO.insertCrew(crewList) // Inserta datos y extrae resultado con <-
    _ <- IO.println(s"Registros insertados: ${resultCrew.size}") // Imprime cantidad
    //    resultRatings <- MoviesDAO.insertRatings(ratingsList) // Inserta datos y extrae resultado con <-
    //    _ <- IO.println(s"Registros insertados: ${resultRatings.size}") // Imprime cantidad
    resultPeliculas <- MoviesDAO.insertPeliculas(peliculasList) // Inserta datos y extrae resultado con <-
    _ <- IO.println(s"Registros insertados: ${resultPeliculas.size}") // Imprime cantidad
  } yield () // Completa la operación
}
