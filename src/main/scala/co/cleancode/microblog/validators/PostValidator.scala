package co.cleancode.microblog.validators

import co.cleancode.microblog.models.Post

object PostValidator extends BaseValidator {
  def validate(post: Post): Unit = {
    if (post.body == null) {
      throw new RequiredException("body")
    }

    if (post.body.length() > 1024) {
      throw new TooLongException("body", 1024)
    }
  }
}