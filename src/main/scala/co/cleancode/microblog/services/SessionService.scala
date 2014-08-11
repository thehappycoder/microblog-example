package co.cleancode.microblog.services

import scala.slick.driver.H2Driver.simple.Session
import com.github.t3hnar.bcrypt._
import co.cleancode.microblog.db.dao.SessionDAO
import co.cleancode.microblog.models.{ Session ⇒ SessionModel }
import java.util.UUID
import java.sql.Timestamp
import java.util.Date
import co.cleancode.microblog.models.User

object SessionService extends BaseService {
  def add(userId: Long): String = {
    withTransaction { implicit session ⇒
      /* Log out if logged in */
      SessionDAO.deleteByUserId(userId)

      /* Log in */
      val token = UUID.randomUUID.toString
      val now = new Timestamp(new Date().getTime())
      SessionDAO.add(SessionModel(None, token, userId, now, now))
      token
    }
  }

  /*
   * Authenticates user by given token.
   * Session timeouts are implemented by background process that delete old sessions from DB.
   */
  def authenticate(token: String): Option[User] = {
    withTransaction { implicit session ⇒
      SessionDAO.getUserIdByToken(token) match {
        case Some(userId) ⇒ Some(UserService.getById(userId))
        case None ⇒ None
      }
    }
  }
}