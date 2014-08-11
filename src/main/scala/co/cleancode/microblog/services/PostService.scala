package co.cleancode.microblog.services

import scala.slick.driver.H2Driver.simple.Session
import co.cleancode.microblog.models.Post
import co.cleancode.microblog.db.dao.PostDAO
import co.cleancode.microblog.db.tables.Posts
import scala.slick.lifted.TableQuery
import co.cleancode.microblog.validators.PostValidator

object PostService extends BaseService {
  import PostService._
  import PostValidator._

  def getPostOwner(id: Long): Long = {
    withTransaction { implicit session ⇒
      PostDAO.getPostOwner(id)
    }
  }

  def list(userId: Option[Long], after: Long, limit: Int): List[Post] = {
    withTransaction { implicit session ⇒
      PostDAO.list(userId, after, limit)
    }
  }

  def update(post: Post): Post = {
    validate(post)

    withTransaction { implicit session ⇒
      PostDAO.update(post)
    }
  }

  def add(post: Post): Post = {
    validate(post)

    withTransaction { implicit session ⇒
      PostDAO.add(post)
    }
  }

  def delete(id: Long): Unit = {
    withTransaction { implicit session ⇒
      PostDAO.delete(id)
    }
  }
}