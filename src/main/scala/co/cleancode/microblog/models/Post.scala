package co.cleancode.microblog.models

import java.sql.Timestamp

case class Post(id: Option[Long], body: String, userId: Long, createdAt: Timestamp, updatedAt: Timestamp) extends Model[Post] {
  def copyWithId(id: Option[Long]) = copy(id = id)
}