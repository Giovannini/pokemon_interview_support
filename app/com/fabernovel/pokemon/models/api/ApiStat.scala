package com.fabernovel.pokemon.models.api

import java.net.URL

import play.api.libs.json.{Json, OFormat}
import com.fabernovel.pokemon.utils.JsonHelpers.urlFormat

final case class StatDefinition(name: String, url: URL)

final case class ApiStat(
  base_stat: Int,
  stat: StatDefinition,
  averageStat: Int
)

object StatDefinition {
  implicit val format: OFormat[StatDefinition] =
    Json.format[StatDefinition]
}

object ApiStat {
  implicit val format: OFormat[ApiStat] =
    Json.format[ApiStat]
}
