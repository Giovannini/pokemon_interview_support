package com.fabernovel.pokemon.models.api

import play.api.libs.json.{Json, OFormat}

final case class PokemonApiResponse(
  id: Int,
  name: String,
  sprites: ApiSprites,
  stats: Seq[ApiStat],
  types: Seq[ApiType]
)

object PokemonApiResponse {
  implicit val format: OFormat[PokemonApiResponse] =
    Json.format[PokemonApiResponse]
}
