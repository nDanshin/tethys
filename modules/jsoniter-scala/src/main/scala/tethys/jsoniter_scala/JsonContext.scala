package tethys.jsoniter_scala

// todo: можно и нужно ли сделать так, чтобы этот вспомогательный трейт потом не торчал наружу
sealed trait JsonContext
object JsonContext {
  case object Object extends JsonContext
  case object Field extends JsonContext
  case object Array extends JsonContext
}
