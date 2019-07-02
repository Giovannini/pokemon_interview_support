package com.fabernovel.pokemon.utils

import java.net.URL

import play.api.libs.json.{Format, JsError, JsPath, JsResult, JsString, JsSuccess, JsValue}

import scala.util.Try

object JsonHelpers {

  implicit val urlFormat: Format[URL] = new Format[URL] {
    override def reads(json: JsValue): JsResult[URL] =
      json.validate[String].flatMap { string =>
        Try(new URL(string)) match {
          case scala.util.Success(url) => JsSuccess(url)
          case _ => JsError(JsPath(), "Unable to parse value as a proper URL.")
        }
      }

    override def writes(o: URL): JsValue = JsString(o.toString)
  }
}
