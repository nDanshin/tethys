package tethys.readers

import scala.util.control.NonFatal

final class ReaderError protected(message: String, cause: Throwable, field: String) extends Exception(message, cause)

object ReaderError {
  def wrongJson(reason: String)(implicit fieldName: FieldName): Nothing = {
    val field = fieldName.value()
    throw new ReaderError(
      message = s"Json is not properly formatted '$field': $reason",
      cause = null,
      field = field
    )
  }

  def catchNonFatal[A](fun: => A)(implicit fieldName: FieldName): Either[ReaderError, A] = {
    try Right(fun) catch {
      case err: ReaderError => Left(err)
      case NonFatal(e) => Left(new ReaderError(
        message = e.getMessage,
        cause = e,
        field = fieldName.value())
      )
    }
  }
}