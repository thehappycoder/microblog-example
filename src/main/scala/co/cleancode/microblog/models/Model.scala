package co.cleancode.microblog.models

trait Model[M] {
  def copyWithId(id: Option[Long]): M
  def id: Option[Long]
}