package co.cleancode.microblog.services

import scala.slick.driver.H2Driver.simple.Session
import co.cleancode.microblog.models.User
import co.cleancode.microblog.db.dao.UserDAO
import co.cleancode.microblog.validators.UserValidator
import com.github.t3hnar.bcrypt._

object UserService extends BaseService {
  import UserValidator._

  private val salt = "$2a$10$isselEovmkJELw2MkzyQiO"

  def getById(id: Long): User = withTransaction { implicit session ⇒
    UserDAO.getById(id)
  }

  def add(user: User): User = {
    validate(user)

    withTransaction { implicit session ⇒
      // Encryption is not production ready, of course
      UserDAO.add(user.copy(password = user.password.bcrypt(salt)))
    }
  }

  def getIdBy(email: String, password: String): Option[Long] = {
    withTransaction { implicit session ⇒
      Option(UserDAO.getIdBy(email, password.bcrypt(salt)))
    }
  }
}