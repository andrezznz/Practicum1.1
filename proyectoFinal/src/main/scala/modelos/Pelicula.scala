package modelos

case class Pelicula(
                     adult: Boolean,
                     budget: Int,
                     homepage: String,
                     id: Int,
                     imdb_id: String,
                     original_language: String,
                     original_title: String,
                     overview: String,
                     popularity: Double,
                     poster_path: String,
                     release_date: String,
                     revenue: Long,
                     runtime: Int,
                     status: String,
                     tagline: String,
                     title: String,
                     video: Boolean,
                     vote_average: Double,
                     vote_count: Int
                   )
