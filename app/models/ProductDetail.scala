package models

import play.api.libs.json.Json

/**
  * Created by vigbokwe on 14/03/2018 at 6:31 PM.
  */
case class ProductDetail(
  key: String,
  value: String,
  productId: Option[Long],
  id: Option[Long] = None
)

object ProductDetail {
  implicit val productDetailFormat = Json.format[ProductDetail]
}
