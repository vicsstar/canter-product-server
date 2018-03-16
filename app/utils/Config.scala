package utils

/**
  * Created by vigbokwe on 14/03/2018 at 7:50 PM.
  */
object Config {
  @javax.inject.Singleton
  @scala.annotation.implicitNotFound("No implicit paging parameter (page and max) found here.")
  class PagingParams(page: Option[Int], max: Option[Int])
}
