package co.cleancode.microblog.api.routes

import spray.routing.HttpService
import akka.actor.Props
import co.cleancode.microblog.api.services.UserApiService
import co.cleancode.microblog.api.services.PostApiService
import co.cleancode.microblog.api.services.SessionApiService
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.Future
import spray.routing.AuthenticationFailedRejection
import co.cleancode.microblog.models.User
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await

trait BaseRoutes extends HttpService {
  val userService = actorRefFactory.actorOf(Props[UserApiService], "userService")
  val postService = actorRefFactory.actorOf(Props[PostApiService], "postService")
  val sessionService = actorRefFactory.actorOf(Props[SessionApiService], "sessionService")

  implicit val timeout = Timeout(5 seconds)

  private def validationResult(userFuture: Future[Option[User]]): Future[Either[AuthenticationFailedRejection, User]] = userFuture map {
    case Some(user) ⇒ Right(user)
    case None ⇒ Left(AuthenticationFailedRejection(AuthenticationFailedRejection.CredentialsRejected, Nil))
  }

  protected def validate(token: String): Future[Either[AuthenticationFailedRejection, User]] =
    validationResult((sessionService ? SessionApiService.Authenticate(token)).mapTo[Option[User]])

  protected def validatePostOwner(token: String, postId: Long): Future[Either[AuthenticationFailedRejection, User]] = {
    val authenticationFuture = sessionService ? SessionApiService.Authenticate(token)
    val postOwnerFuture = postService ? PostApiService.GetPostOwner(postId)

    val resultFuture = for {
      user ← authenticationFuture.mapTo[Option[User]]
      postUserId ← postOwnerFuture.mapTo[Long]
    } yield if (user.nonEmpty && user.get.id.get == postUserId)
      user
    else
      None

    validationResult(resultFuture)
  }
}