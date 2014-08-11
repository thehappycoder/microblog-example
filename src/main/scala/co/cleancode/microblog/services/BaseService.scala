package co.cleancode.microblog.services

import co.cleancode.microblog.db.Databases
import scala.slick.driver.H2Driver.simple._

object BaseService {
  val mainDb = Databases.main
}

trait BaseService {
  import BaseService._

  protected[services] def withTransaction[T](f: Session ⇒ T): T = {
    mainDb.withTransaction { implicit session ⇒
      f(session)
    }
  }
}