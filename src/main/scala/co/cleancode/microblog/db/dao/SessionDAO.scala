package co.cleancode.microblog.db.dao

import scala.slick.lifted.TableQuery
import scala.slick.driver.H2Driver.simple._
import co.cleancode.microblog.db.tables.Sessions
import co.cleancode.microblog.models.{ Session ⇒ SessionModel }
import org.postgresql.util.PSQLException

object SessionDAO extends BaseDAO[SessionModel, Sessions] {
  protected val table = TableQuery[Sessions]

  def deleteByUserId(userId: Long)(implicit session: Session): Unit = {
    val query = for { row ← table if row.userId === userId } yield row
    query.delete
  }

  def getUserIdByToken(token: String)(implicit session: Session): Option[Long] = {
    val query = for { row ← table if row.token === token } yield row.userId
    query.firstOption
  }
}