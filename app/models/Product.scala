package models

import play.api.libs.json._

/**
  * Created by vigbokwe on 14/03/2018 at 4:01 PM.
  */
case class Product(
  name: String,
  category: String,
  code: String,
  price: Double,
  id: Option[Long] = None
)

object Product {
  implicit val productFormat = Json.format[Product]
}