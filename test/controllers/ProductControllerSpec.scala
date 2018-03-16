package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class ProductControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "ProductController GET" should {

    "render the index page from a new instance of controller" in {
//      val controller = new ProductController(stubControllerComponents())
//      val home = controller.index().apply(FakeRequest(GET, "/"))
//
//      status(home) mustBe OK
//      contentType(home) mustBe Some("text/html")
//      contentAsString(home) must include ("Welcome to Play")
    }

    "render a list of products' JSON" in {
      val controller = inject[ProductController]
      val products = controller.index().apply(FakeRequest(GET, "/products"))

      status(products) mustBe OK
      contentType(products) mustBe Some("application/json")
      contentAsString(products) must include ("[]")
    }

    "render the product details JSON" in {
      val request = FakeRequest(GET, "/products/1")
      val product = route(app, request).get

      status(product) mustBe OK
      contentType(product) mustBe Some("application/json")
      contentAsString(product) must include ("Welcome to Play")
    }
  }
}
