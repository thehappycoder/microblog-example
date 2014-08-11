package co.cleancode.microblog.api.model

import co.cleancode.microblog.validators.ValidationException

case class ValidationFailedResponse[T](e: T) {
  def errorCode = e.getClass.getSimpleName
}