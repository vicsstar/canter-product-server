import com.google.inject.AbstractModule
import models.ProductRepository

/**
  * Created by vigbokwe on 14/03/2018 at 7:54 PM.
  */
class Module extends AbstractModule {
  def configure(): Unit = {
    // bind singletons.
    bind(classOf[ProductRepository]).asEagerSingleton()
  }
}
