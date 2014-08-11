package co.cleancode.microblog.api

import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import spray.can.Http
import akka.actor.actorRef2Scala

object Boot extends App {

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("microblog")

  // create and start our service actor
  val service = system.actorOf(Props[ApiServiceActor], "api-service")

  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(service, interface = "localhost", port = 8080)
}