import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import java.io.File

// Clase para representar las películas
case class Peliculas(
                      adult: Boolean,
                      belongs_to_collection: String,
                      budget: Int,
                      genres: String,
                      homepage: String,
                      id: Int,
                      imdb_id: String,
                      original_language: String,
                      original_title: String,
                      overview: String,
                      popularity: Float,
                      poster_path: String,
                      production_companies: String,
                      production_countries: String,
                      release_date: String,
                      revenue: Int,
                      runtime: Int,
                      spoken_languages: String,
                      status: String,
                      tagline: String,
                      title: String,
                      video: Boolean,
                      vote_average: Float,
                      vote_count: Int,
                      keywords: String,
                      cast: String,
                      crew: String,
                      ratings: String
                    )

object PeliculasStats extends App {

  // Definición del archivo CSV
  val filePath = "Data/pi_movies_small.csv"

  // Carga de datos desde el CSV
  val dataSource = new File(filePath).readCsv[List, Peliculas](rfc.withHeader.withCellSeparator(';'))
  val peliculas = dataSource.collect { case Right(pelicula) => pelicula }

  // ================= FUNCIONES AUXILIARES =================

  /** Calcula estadísticas descriptivas de una lista de números. */
  def calcularEstadisticas(datos: Seq[Double]): Map[String, Double] = {
    if (datos.isEmpty) {
      Map("count" -> 0, "mean" -> 0, "min" -> 0, "max" -> 0, "stddev" -> 0)
    } else {
      val mean = datos.sum / datos.size
      val variance = datos.map(x => math.pow(x - mean, 2)).sum / datos.size
      Map(
        "count" -> datos.size,
        "mean" -> mean,
        "min" -> datos.min,
        "max" -> datos.max,
        "stddev" -> math.sqrt(variance)
      )
    }
  }

  /** Genera una tabla formateada para estadísticas. */
  def generarTabla(estadisticas: Map[String, Map[String, Double]]): Unit = {
    // Definir los encabezados
    val headers = Seq("Columna", "Count", "Mean", "Min", "Max", "StdDev")

    // Crear filas para cada estadística
    val rows = estadisticas.map { case (columna, stats) =>
      Seq(
        columna,
        stats("count").toInt.toString,
        f"${stats("mean")}%.2f",
        f"${stats("min")}%.2f",
        f"${stats("max")}%.2f",
        f"${stats("stddev")}%.2f"
      )
    }.toSeq

    // Calcular anchos máximos para alinear columnas
    val colWidths = (headers +: rows).transpose.map(_.map(_.length).max)

    // Función para formatear una fila con anchos alineados
    def formatRow(row: Seq[String]): String = {
      row.zip(colWidths).map { case (item, width) => item.padTo(width, ' ') }.mkString(" | ")
    }

    // Generar la tabla con separadores
    val separator = colWidths.map("-" * _).mkString("-+-")
    val table = (Seq(formatRow(headers), separator) ++ rows.map(formatRow)).mkString("\n")

    println(table)
  }

  // ================= ANÁLISIS =================

  // Cálculo de estadísticas para columnas numéricas
  val estadisticas = Map(
    "Budget" -> calcularEstadisticas(peliculas.map(_.budget.toDouble).filter(_ >= 0)),
    "Popularity" -> calcularEstadisticas(peliculas.map(_.popularity.toDouble).filter(_ >= 0)),
    "Revenue" -> calcularEstadisticas(peliculas.map(_.revenue.toDouble).filter(_ >= 0)),
    "Runtime" -> calcularEstadisticas(peliculas.map(_.runtime.toDouble).filter(_ >= 0)),
    "Vote Average" -> calcularEstadisticas(peliculas.map(_.vote_average.toDouble).filter(_ >= 0)),
    "Vote Count" -> calcularEstadisticas(peliculas.map(_.vote_count.toDouble).filter(_ >= 0))
  )

  // Generar y mostrar la tabla
  generarTabla(estadisticas)
}
