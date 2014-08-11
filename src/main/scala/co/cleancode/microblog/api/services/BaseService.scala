package co.cleancode.microblog.api.services

import co.cleancode.microblog.api.marshallers.JsonFormats
import spray.routing.RequestContext

object BaseApiService {
}

trait BaseApiService extends JsonFormats {
  protected def handle[R](requestContext: RequestContext)(f: () ⇒ Unit): Unit = {
    try {
      f()
    } catch {
      case e: Exception ⇒
        requestContext.failWith(e)
    }
  }
}