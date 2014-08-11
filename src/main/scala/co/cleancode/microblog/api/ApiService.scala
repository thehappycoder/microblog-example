package co.cleancode.microblog.api

import akka.actor.Actor
import spray.routing._
import spray.http._
import spray.http.MediaTypes._
import co.cleancode.microblog.api.routes._
import spray.util.LoggingContext
import org.postgresql.util.PSQLException
import co.cleancode.microblog.api.marshallers.JsonFormats
import co.cleancode.microblog.api.model.OperationError
import co.cleancode.microblog.validators.ValidationException
import co.cleancode.microblog.validators.TooLongException
import co.cleancode.microblog.api.model.ValidationFailedResponse

class ApiServiceActor extends Actor with HttpService with UserRoutes with PostRoutes with SessionRoutes with JsonFormats {
  implicit def myExceptionHandler(implicit log: LoggingContext) =
    ExceptionHandler {
      case e: ValidationException ⇒ {
        complete(422, ValidationFailedResponse(e))
      }
      case e: PSQLException ⇒ {
        e.getSQLState() match {
          case "23505" ⇒
            complete(409, OperationError.entityAlreadyExists)
          case _ ⇒
            logException(e)
            complete(500, "Server error")
        }
      }
      case e: Exception ⇒ {
        logException(e)
        complete(500, "Server error")
      }
    }

  private def logException(e: Exception)(implicit log: LoggingContext) =
    log.error("%s %n%s %n%s".format(e.getMessage, e.getStackTraceString, e.getCause))

  def actorRefFactory = context

  def receive = runRoute(userRoutes ~ postRoutes ~ sessionRoutes)

  /*
  implicit val rejectionHandler = RejectionHandler {
    case MissingCookieRejection(cookieName) :: _ ⇒
      complete(BadRequest, "No cookies, no service!!!")
  }*/

}