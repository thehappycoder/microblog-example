package co.cleancode.microblog.db

import scala.slick.driver.H2Driver.simple.Database

object Databases {
  // TODO move configuration to properties
  val main = Database.forURL("jdbc:postgresql:microblog", driver = "org.postgresql.Driver")
}