package co.cleancode.microblog.db.tables

import scala.slick.driver.H2Driver.simple._
import co.cleancode.microblog.models.Post
import java.sql.Timestamp

class Posts(tag: Tag) extends BaseTable[Post](tag, "posts") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def body = column[String]("body", O.NotNull)
  def userId = column[Long]("user_id", O.NotNull)
  def createdAt = column[Timestamp]("created_at", O.NotNull)
  def updatedAt = column[Timestamp]("updated_at", O.NotNull)
  def * = (id.?, body, userId, createdAt, updatedAt) <> (Post.tupled, Post.unapply)
}