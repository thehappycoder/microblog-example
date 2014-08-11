package co.cleancode.microblog.api.model

object OperationError {
  val entityAlreadyExists = OperationError("EntityAlreadyExists", "Entity already exists")
  val credentialsNoMatch = OperationError("CredentialsNoMatch", "Credentials do not match")
}

case class OperationError(code: String, message: String)