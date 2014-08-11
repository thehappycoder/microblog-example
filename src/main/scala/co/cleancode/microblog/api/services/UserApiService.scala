package co.cleancode.microblog.api.services

import co.cleancode.microblog.models.User
import akka.actor.Actor
import co.cleancode.microblog.services.UserService
import spray.routing.RequestContext

object UserApiService {
  case class Add(user: User, requestContext: RequestContext)
}

class UserApiService extends Actor with BaseApiService {
  import UserApiService._
  import UserService._

  def receive = {
    case Add(user, requestContext) ⇒ handle(requestContext) { () ⇒
      requestContext.complete(add(user))
    }

    case _ ⇒ throw new IllegalArgumentException("Unknown message")
  }
}