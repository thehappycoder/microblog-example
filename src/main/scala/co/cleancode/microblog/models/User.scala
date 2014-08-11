package co.cleancode.microblog.models

case class User(id: Option[Long], email: String, password: String, firstName: String, lastName: String) extends Model[User] {
  def copyWithId(id: Option[Long]) = copy(id = id)
}