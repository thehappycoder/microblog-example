package co.cleancode.microblog.api.marshallers

import co.cleancode.microblog.models.Post
import co.cleancode.microblog.models.User
import spray.json._
import DefaultJsonProtocol._
import java.sql.Timestamp
import co.cleancode.microblog.api.model.OperationSuccess
import co.cleancode.microblog.api.model.OperationError
import co.cleancode.microblog.api.model.DeletionSuccess
import co.cleancode.microblog.api.model.DeletionSuccess
import spray.httpx.SprayJsonSupport
import co.cleancode.microblog.validators.TooLongException
import co.cleancode.microblog.api.model.ValidationFailedResponse
import co.cleancode.microblog.validators.ValidationException
import co.cleancode.microblog.validators.TooLongException
import co.cleancode.microblog.validators.RequiredException

trait JsonFormats extends DefaultJsonProtocol with NullOptions with SprayJsonSupport {
  implicit object TimestampFormat extends JsonFormat[Timestamp] {
    def write(obj: Timestamp) = Option(obj).map((t: Timestamp) ⇒ JsNumber(t.getTime)).getOrElse(JsNull)

    def read(json: JsValue) = json match {
      case JsNumber(time) ⇒ new Timestamp(time.toLong)
      case _ ⇒ throw new DeserializationException("Date expected")
    }
  }

  implicit object UserFormat extends RootJsonFormat[User] {
    def write(user: User) = JsObject(
      "id" -> user.id.map(JsNumber(_)).getOrElse(JsNull),
      "email" -> JsString(user.email),
      "firstName" -> JsString(user.firstName),
      "lastName" -> JsString(user.lastName))

    def read(value: JsValue) = {
      value.asJsObject.getFields("id", "email", "firstName", "lastName") match {
        case Seq(JsNumber(id), JsString(email), JsString(firstName), JsString(lastName)) ⇒
          User(Some(id.toLong), email.toString, null, firstName.toString, lastName.toString)
        case _ ⇒ throw new DeserializationException("User expected")
      }
    }
  }

  implicit def validationFailedResponseFormat[T: JsonFormat] = new RootJsonFormat[ValidationFailedResponse[T]] {
    def write(obj: ValidationFailedResponse[T]) = JsObject(
      "success" -> JsBoolean(false),
      "errorCode" -> JsString(obj.errorCode),
      "errorData" -> obj.e.toJson)

    def read(value: JsValue) = throw new DeserializationException("Unsupported")
  }

  implicit object ValidationExceptionFormat extends JsonFormat[ValidationException] {
    def write(obj: ValidationException) = obj match {
      case e: RequiredException ⇒ e.toJson
      case e: TooLongException ⇒ e.toJson
    }

    def read(value: JsValue) = throw new DeserializationException("Unsupported")
  }

  implicit def operationSuccessFormat[T: JsonFormat] = new RootJsonFormat[OperationSuccess[T]] {
    def write(obj: OperationSuccess[T]) = JsObject(
      "success" -> JsBoolean(true),
      "data" -> obj.data.toJson)

    def read(value: JsValue) = throw new DeserializationException("Unsupported")
  }

  implicit object DeletionSuccessFormat extends RootJsonFormat[DeletionSuccess] {
    def write(obj: DeletionSuccess) = JsObject("success" -> JsBoolean(true))

    def read(value: JsValue) = throw new DeserializationException("Unsupported")
  }

  implicit object operationErrorFormat extends RootJsonFormat[OperationError] {
    def write(obj: OperationError) = JsObject(
      "success" -> JsBoolean(false),
      "code" -> JsString(obj.code),
      "message" -> JsString(obj.message))

    def read(value: JsValue) = throw new DeserializationException("Unsupported")
  }

  implicit val requiredExceptionFormat = jsonFormat1(RequiredException)
  implicit val tooLongExceptionFormat = jsonFormat2(TooLongException)

  implicit val postFormat = jsonFormat(Post, "id", "body", "userId", "createdAt", "updatedAt")
}