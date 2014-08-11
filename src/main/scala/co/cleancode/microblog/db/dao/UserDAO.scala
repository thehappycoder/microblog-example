package co.cleancode.microblog.db.dao

import scala.slick.lifted.TableQuery
import scala.slick.driver.H2Driver.simple._
import co.cleancode.microblog.db.tables.Users
import co.cleancode.microblog.models.User
import org.postgresql.util.PSQLException

object UserDAO extends BaseDAO[User, Users] {
  protected val table = TableQuery[Users]

  def getIdBy(email: String, password: String)(implicit session: Session): Long =
    table.filter(r ⇒ r.email === email && r.password === password).map(_.id).first
}