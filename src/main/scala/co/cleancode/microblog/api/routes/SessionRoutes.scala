package co.cleancode.microblog.api.routes

import co.cleancode.microblog.models.User
import co.cleancode.microblog.api.services.SessionApiService

trait SessionRoutes extends BaseRoutes {
  val sessionRoutes =
    path("sessions") {
      post {
        anyParams("email", "password") { (email, password) ⇒
          requestContext ⇒
            sessionService ! SessionApiService.Login(email, password, requestContext)
        }
      }
    }
}