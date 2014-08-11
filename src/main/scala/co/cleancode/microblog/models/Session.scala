package co.cleancode.microblog.models

import java.sql.Timestamp

case class Session(id: Option[Long], token: String, userId: Long, createdAt: Timestamp, updatedAt: Timestamp) extends Model[Session] {
  def copyWithId(id: Option[Long]) = copy(id = id)
}