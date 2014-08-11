package co.cleancode.microblog.db.tables

import scala.slick.driver.H2Driver.simple._
import co.cleancode.microblog.models.User

class Users(tag: Tag) extends BaseTable[User](tag, "users") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def email = column[String]("email", O.NotNull)
  def password = column[String]("password", O.NotNull)
  def firstName = column[String]("first_name", O.NotNull)
  def lastName = column[String]("last_name", O.NotNull)
  def * = (id.?, email, password, firstName, lastName) <> (User.tupled, User.unapply)
}