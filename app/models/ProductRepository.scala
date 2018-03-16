package models

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.JdbcProfile
import utils.Props

/**
  * Created by vigbokwe on 14/03/2018 at 6:40 PM.
  */
@Singleton
class ProductRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  // get the JdbcProfile for this provider.
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  // import necessary variable and DSLs for creating and using the table.
  import dbConfig._
  import profile.api._

  private class ProductTable(tag: Tag) extends Table[Product](tag, "products") {
    def id = column[Long](Props.ID, O.PrimaryKey, O.AutoInc)
    def name = column[String](Props.NAME)
    def category = column[String](Props.CATEGORY)
    def code = column[String](Props.CODE)
    def price = column[Double](Props.PRICE)
    def * = (name, category, code, price, id.?) <> ((Product.apply _).tupled, Product.unapply)
  }

  /**
    * Starting point for queries to our "products" table.
    */
  private val products = TableQuery[ProductTable]
  private val productDetails = TableQuery[ProductDetailTable]

  private class ProductDetailTable(tag: Tag) extends Table[ProductDetail](tag, "product_details") {
    def id = column[Long](Props.ID, O.PrimaryKey, O.AutoInc)
    def key = column[String](Props.KEY)
    def value = column[String](Props.VALUE)
    def productId = column[Option[Long]](Props.PRODUCT_ID)
    def * = (key, value, productId, id.?) <> ((ProductDetail.apply _).tupled, ProductDetail.unapply)

    def product = foreignKey("fk_product_details__product_id",
      productId, products)(_.id.?, onDelete = ForeignKeyAction.Cascade)
  }

  def create(product: Product): Future[Product] = db.run {
    (products
      returning products.map(_.id)
      into((product, id) => product.copy(id = Some(id)))
    ) += product
  }

  def update(product: Product): Future[Int] = db.run {
    products.filter(_.id === product.id).update(product)
  }

  def delete(product: Product): Future[Int] = db.run {
    (for {
      p <- products
      if p.id === product.id
    } yield p)
      .delete
  }

  def list(): Future[Seq[Product]] = db.run {
    (for (product <- products) yield product).result
  }

  def get(productId: Long): Future[Option[Product]] = db.run {
    products.filter(_.id === productId).result.headOption
  }

  def saveDetail(detail: ProductDetail): Future[ProductDetail] = db.run {
    (productDetails
      returning productDetails.map(_.id)
      into((detail, id) => detail.copy(id = Some(id)))
    ) += detail
  }

  // product details.
  def allDetailsByProduct(product: Product): Future[Seq[ProductDetail]] = db.run {
    (for {
      detail <- productDetails
      if detail.productId === product.id
    } yield detail).result
  }
}
