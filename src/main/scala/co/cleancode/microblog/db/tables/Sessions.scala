package co.cleancode.microblog.db.tables

import scala.slick.driver.H2Driver.simple._
import java.sql.Timestamp
import co.cleancode.microblog.models.Session

class Sessions(tag: Tag) extends BaseTable[Session](tag, "sessions") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def token = column[String]("token", O.NotNull)
  def userId = column[Long]("user_id", O.NotNull)
  def createdAt = column[Timestamp]("created_at", O.NotNull)
  def lastActivityAt = column[Timestamp]("last_activity_at", O.NotNull)
  def * = (id.?, token, userId, createdAt, lastActivityAt) <> (Session.tupled, Session.unapply)
}