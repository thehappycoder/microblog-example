package co.cleancode.microblog.api.services

import akka.actor.Actor
import co.cleancode.microblog.services.UserService
import spray.routing.RequestContext
import co.cleancode.microblog.api.model.OperationSuccess
import co.cleancode.microblog.api.model.OperationError
import co.cleancode.microblog.services.SessionService
import akka.actor.ActorLogging

object SessionApiService {
  case class Login(email: String, password: String, requestContext: RequestContext)
  case class Authenticate(token: String)
}

class SessionApiService extends Actor with BaseApiService with ActorLogging {
  import SessionApiService._

  def receive = {
    case Login(email, password, requestContext) ⇒ handle(requestContext) { () ⇒
      UserService.getIdBy(email, password) match {
        case Some(userId) ⇒ requestContext.complete(OperationSuccess(SessionService.add(userId)))
        case None ⇒ requestContext.complete(OperationError.credentialsNoMatch)
      }
    }

    case Authenticate(token: String) ⇒ {
      sender ! SessionService.authenticate(token)
    }

    case _ ⇒ throw new IllegalArgumentException("Unknown message")
  }
}