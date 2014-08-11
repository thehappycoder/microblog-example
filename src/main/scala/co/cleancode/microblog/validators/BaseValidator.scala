package co.cleancode.microblog.validators

abstract class ValidationException extends RuntimeException
abstract class PropertyValidationException(propertyName: String) extends ValidationException

case class RequiredException(propertyName: String) extends PropertyValidationException(propertyName)
case class TooLongException(propertyName: String, limit: Int) extends PropertyValidationException(propertyName)

trait BaseValidator {

}