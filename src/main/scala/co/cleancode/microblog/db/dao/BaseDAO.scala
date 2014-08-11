package co.cleancode.microblog.db.dao

import scala.slick.lifted.TableQuery
import scala.slick.driver.H2Driver.simple._
import co.cleancode.microblog.models.Model
import co.cleancode.microblog.db.tables.BaseTable

trait BaseDAO[M <: Model[M], T <: BaseTable[M]] {
  protected val table: TableQuery[T]

  def getById(id: Long)(implicit session: Session): M = {
    val query = for { row ← table if row.id === id } yield row
    query.first
  }

  def delete(id: Long)(implicit session: Session): Unit = {
    val query = for { row ← table if row.id === id } yield row
    query.delete
  }

  /**
   * Inserts a new row with data from given <code>model</code> and
   * returns added model with auto generated id.
   */
  def add(model: M)(implicit session: Session): M = {
    val id = (table returning table.map(_.id)) += model
    model.copyWithId(Some(id))
  }

  def update(model: M)(implicit session: Session): M = {
    val query = for { row ← table if row.id === model.id } yield row

    if (query.update(model) != 1) {
      throw new OperationFailed
    }

    model
  }

  class OperationFailed extends RuntimeException
}