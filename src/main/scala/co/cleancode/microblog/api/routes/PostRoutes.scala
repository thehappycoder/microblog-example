package co.cleancode.microblog.api.routes

import co.cleancode.microblog.api.services.PostApiService
import co.cleancode.microblog.models.Post
import co.cleancode.microblog.models.User
import co.cleancode.microblog.api.services.SessionApiService
import scala.concurrent.ExecutionContext.Implicits.global

trait PostRoutes extends BaseRoutes {
  import PostApiService._

  val postRoutes =
    path("posts") {
      get {
        parameters("after".as[Long] ? 0, "limit".as[Int] ? 30) { (after, limit) ⇒
          requestContext ⇒
            postService ! List(after, limit, None, requestContext)
        }
      } ~
        post {
          headerValueByName("auth_token") { authToken ⇒
            anyParams("body") { body ⇒
              authenticate(validate(authToken)) { user ⇒
                requestContext ⇒
                  postService ! Add(Post(None, body, user.id.get, null, null), requestContext)
              }
            }
          }
        }
    } ~ pathPrefix("posts") {
      path(IntNumber) { id ⇒
        headerValueByName("auth_token") { authToken ⇒
          put {
            anyParams("body") { body ⇒
              authenticate(validatePostOwner(authToken, id)) { user ⇒
                requestContext ⇒
                  postService ! Update(Post(Some(id), body, user.id.get, null, null), requestContext)
              }
            }
          } ~ delete {
            authenticate(validatePostOwner(authToken, id)) { user ⇒
              requestContext ⇒
                postService ! Delete(id, requestContext)
            }
          }
        }
      }
    }
}