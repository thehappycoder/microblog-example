package co.cleancode.microblog.db.tables

import co.cleancode.microblog.models.Model
import scala.slick.driver.H2Driver.simple._

abstract class BaseTable[M <: Model[M]](tag: Tag, tableName: String) extends Table[M](tag, tableName) {
  def id: Column[Long]
}