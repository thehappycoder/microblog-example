package co.cleancode.microblog.api.services

import co.cleancode.microblog.models.Post
import akka.actor.Actor
import co.cleancode.microblog.services.PostService
import spray.routing.RequestContext
import co.cleancode.microblog.api.model.DeletionSuccess
import co.cleancode.microblog.api.marshallers.JsonFormats
import akka.actor.ActorLogging

object PostApiService {
  case class List(after: Long, limit: Int, userId: Option[Long] = None, requestContext: RequestContext)
  case class Update(post: Post, requestContext: RequestContext)
  case class Add(post: Post, requestContext: RequestContext)
  case class Delete(id: Long, requestContext: RequestContext)
  case class GetPostOwner(id: Long)
}

class PostApiService extends Actor with BaseApiService with ActorLogging {
  import PostApiService._
  import PostService._

  def receive = {
    case List(after: Long, limit: Int, userId: Option[Long], requestContext) ⇒ handle(requestContext) { () ⇒
      requestContext.complete(list(userId, after, limit))
    }

    case Update(post, requestContext) ⇒ handle(requestContext) { () ⇒
      requestContext.complete(update(post))
    }

    case Add(post, requestContext) ⇒ handle(requestContext) { () ⇒
      requestContext.complete(add(post))
    }

    case Delete(id, requestContext) ⇒ handle(requestContext) { () ⇒
      delete(id)
      requestContext.complete(DeletionSuccess())
    }

    case GetPostOwner(id) ⇒ {
      sender ! getPostOwner(id)
    }

    case _ ⇒ throw new IllegalArgumentException("Unknown message")
  }
}