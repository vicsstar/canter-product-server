package controllers

import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.libs.json.Json.JsValueWrapper

/**
  * Created by vigbokwe on 16/03/2018 at 4:06 AM.
  */
trait Helpers {
  /**
    * Implicit converter for JsValue to a `json` type.
    *
    * @param jsObj - the JsValue
    * @return the `json` type.
    */
  implicit def jsValueToJson(jsObj: (Option[String], JsValue)): json = new json(jsObj)

  /**
    * Class for dealing with json objects.
    *
    * @param baseJs - the base JsValue.
    */
  class json(baseJs: (Option[String], JsValue)) {
    // add new fields into a base JsObject.
    def embedFields(fields : (String, JsValueWrapper)*): JsObject = baseJs match {
      case (keyOpt, baseJson) =>
        baseJson match {
          case baseJson: JsObject =>
            // embed the json fields.
            val resultingJson =
              fields.foldLeft(baseJson)(
                (json, field) =>
                  // combine both fields.
                  JsObject(
                    json.value ++
                      Json.obj(field).value
                  )
              )

            // returning the resulting JSON.
            keyOpt match {
              case Some(key) =>
                Json.obj(key -> resultingJson)
              case _ => resultingJson
            }
          case _ => Json.obj()
        }
    }
  }
}
