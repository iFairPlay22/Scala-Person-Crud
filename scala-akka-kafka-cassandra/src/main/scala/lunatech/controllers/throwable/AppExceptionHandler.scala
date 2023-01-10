package lunatech.controllers.throwable

import akka.http.scaladsl.model.{ HttpResponse, StatusCode }
import akka.http.scaladsl.model.StatusCodes.{ InternalServerError, NoContent }
import akka.http.scaladsl.server
import akka.http.scaladsl.server.ExceptionHandler
import lunatech.Server.complete

object AppExceptionHandler {

  def answer(code: StatusCode, err: Throwable, withMessage: Boolean = false): server.Route = {
    err.printStackTrace()

    if (withMessage)
      complete(HttpResponse(code, entity = err.getMessage))
    else
      complete(HttpResponse(code))
  }

  implicit def exceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case err: NoSuchElementException =>
        answer(NoContent, err)

      case err: IllegalArgumentException =>
        answer(InternalServerError, err, withMessage = true)

      case err: IllegalStateException =>
        answer(InternalServerError, err, withMessage = true)

      case err: Throwable =>
        answer(InternalServerError, err)
    }

}
