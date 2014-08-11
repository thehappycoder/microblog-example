package co.cleancode.microblog.validators

import co.cleancode.microblog.models.User

object UserValidator extends BaseValidator {
  def validate(user: User): Unit = {
    if (user.email == null) {
      throw new RequiredException("email")
    }

    if (user.email.length() > 255) {
      throw new TooLongException("email", 255)
    }

    if (user.firstName == null) {
      throw new RequiredException("firstName")
    }

    if (user.firstName.length() > 255) {
      throw new TooLongException("firstName", 255)
    }

    if (user.lastName == null) {
      throw new RequiredException("lastName")
    }

    if (user.lastName.length() > 255) {
      throw new TooLongException("lastName", 255)
    }
  }
}