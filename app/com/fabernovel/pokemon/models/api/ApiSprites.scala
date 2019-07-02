package com.fabernovel.pokemon.models.api

import play.api.libs.json.{Json, OFormat}

final case class ApiSprites(
  back_default: String,
  front_default: String
)

object ApiSprites {
  implicit val format: OFormat[ApiSprites] =
    Json.format[ApiSprites]
}
