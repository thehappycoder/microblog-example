package co.cleancode.microblog.db.dao

import scala.slick.lifted.TableQuery
import scala.slick.driver.H2Driver.simple._
import co.cleancode.microblog.db.tables.Posts
import co.cleancode.microblog.models.Post
import java.sql.Timestamp
import java.util.Date

object PostDAO extends BaseDAO[Post, Posts] {
  protected val table = TableQuery[Posts]

  def getPostOwner(id: Long)(implicit session: Session) = {
    val query = for { row ← table if row.id === id } yield row.userId
    query.first
  }

  def list(userId: Option[Long], after: Long, limit: Int)(implicit session: Session) = {
    var query = if (after > 0)
      table filter (_.id > after)
    else
      table

    if (userId.nonEmpty)
      query = query filter (_.userId === userId)

    query sortBy (_.createdAt.asc) take (limit) list
  }

  override def add(post: Post)(implicit session: Session): Post = {
    val id = (table.map(r ⇒ (r.body, r.userId)) returning table.map(_.id)) insert (post.body, post.userId)
    getById(id)
  }

  override def update(post: Post)(implicit session: Session): Post = {
    val updatedAt = new Timestamp(new Date().getTime)
    table filter (_.id === post.id) map (r ⇒ (r.body, r.updatedAt)) update ((post.body, updatedAt))
    getById(post.id.get)
  }
}