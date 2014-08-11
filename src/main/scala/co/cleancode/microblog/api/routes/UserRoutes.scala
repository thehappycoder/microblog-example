package co.cleancode.microblog.api.routes

import co.cleancode.microblog.models.User
import co.cleancode.microblog.api.services.UserApiService
import co.cleancode.microblog.api.services.PostApiService

trait UserRoutes extends BaseRoutes {
  val userRoutes =
    path("users") {
      post {
        anyParams("email", "password", "first_name", "last_name") { (email, password, firstName, lastName) ⇒
          requestContext ⇒
            userService ! UserApiService.Add(User(None, email, password, firstName, lastName), requestContext)
        }
      }
    } ~ pathPrefix("users") {
      pathPrefix(IntNumber) { userId ⇒
        path("posts") {
          get {
            parameters("after".as[Long] ? 0, "limit".as[Int] ? 30) { (after, limit) ⇒
              requestContext ⇒
                postService ! PostApiService.List(after, limit, userId = Some(userId), requestContext)
            }
          }
        }
      }
    }
}