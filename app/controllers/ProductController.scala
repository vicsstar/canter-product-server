package controllers

import javax.inject._

import models.{Product, ProductDetail, ProductRepository}
import models.Product.productFormat
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.DurationInt

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ProductController @Inject()(
                                   cc: ControllerComponents,
                                   productRepo: ProductRepository
                                 )(implicit ec: ExecutionContext) extends AbstractController(cc) with Helpers {
  def index() = Action.async {
    productRepo.list().map { products =>
      Ok(Json.toJson(products.map(renderProductAsJson)))
    }
  }

  def get(productId: Long) = Action.async { implicit request =>
    withProduct(productId) { product =>
      Ok(Json.toJson(renderProductAsJson(product)))
    }
  }

  def create = Action(parse.tolerantJson).async { implicit request =>
    // deal with any errors.
    val failure = (errors: Seq[(JsPath, Seq[JsonValidationError])]) => {
      Future.successful(
        BadRequest(Json.obj("error" -> JsError.toJson(errors)))
      )
    }

    // take care of the product retrieved from the request.
    val success = (product: Product) => {
      productRepo.create(product).map { _product =>
        // parse the product details and save them to the database.
        (request.body \ "details").validate[Seq[ProductDetail]] match {
          case JsSuccess(details, _) =>
            details.foreach(detail =>
              // attach the associated product id to each product detail before saving.
              productRepo.saveDetail(detail.copy(productId = _product.id))
            )
            // render the created product and its details.
            Ok(Json.toJson(renderProductAsJson(_product)))

          // Maybe return some more meaningful error?
          case e: JsError => BadRequest(Json.obj("error" -> JsError.toJson(e)))
        }
      }
    }

    request.body
      .validate[Product]
      .fold(failure, success)
  }

  def edit(productId: Long) = Action(parse.tolerantJson).async { implicit request =>
    withProduct(productId) { _ =>
      // deal with any validation errors.
      val failure = (errors: Seq[(JsPath, Seq[JsonValidationError])]) => {
        BadRequest(Json.obj("error" -> JsError.toJson(errors)))
      }

      // update the product retrieved from the request.
      val success = (product: Product) => {
        productRepo.update(product)
        // render the created product and its details.
        Ok(Json.toJson(renderProductAsJson(product)))
      }

      request.body
        .validate[Product]
        .fold(failure, success)
    }
  }

  def delete(productId: Long) = Action.async { implicit request =>
    withProduct(productId) { product =>
      productRepo.delete(product)

      Ok(Json.obj(
        "message" -> messagesApi.preferred(request).apply("product.deleted")
      ))
    }
  }

  /**
    * Helper method that renders a product as JSON by embedding it's details into the initial product JSON.
    *
    * @param product - the product to render as JSON.
    * @return the JSON representation of the product.
    */
  private def renderProductAsJson(product: Product): JsValue = {
    // Get all the product details saved. Let's wait for the results to return.
    // Find a better way to map and extract the product details from the result.
    val _details = Await.result(productRepo.allDetailsByProduct(product), 30.seconds)

    (None -> Json.toJson(product))
      // embed the product details into the product JSON.
      .embedFields("details" -> _details)
  }

  /**
   * Convenience helper method for retrieving a single product
   * from the database or return 404.
   */
  private def withProduct(productId: Long)
                         (action: Product => Result)
                         (implicit request: RequestHeader): Future[Result] = {
    productRepo.get(productId).map {
      case Some(product) => action(product)
      case None => NotFound(
        Json.obj("message" -> messagesApi.preferred(request).apply("product.not-found"))
      )
    }
  }
}
