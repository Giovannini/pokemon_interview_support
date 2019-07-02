package com.fabernovel.pokemon.models.api

import java.net.URL

import play.api.libs.json.{Json, OFormat}
import com.fabernovel.pokemon.utils.JsonHelpers.urlFormat

final case class TypeDefinition(name: String, url: URL)

object TypeDefinition {
  implicit val format: OFormat[TypeDefinition] =
    Json.format[TypeDefinition]
}

final case class ApiType(slot: String, `type`: TypeDefinition)

object ApiType {
  implicit val format: OFormat[ApiType] =
    Json.format[ApiType]
}
