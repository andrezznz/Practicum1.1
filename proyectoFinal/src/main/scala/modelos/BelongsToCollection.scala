package modelos

case class BelongsToCollection(
                                  id: Int,
                                  name: String,
                                  poster_path: Option[String],
                                  backdrop_path: Option[String]
                                )
