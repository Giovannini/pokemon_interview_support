package com.fabernovel.pokemon.services

import java.net.URL

import akka.http.scaladsl.model.MediaTypes
import com.fabernovel.pokemon.models.api.{ApiStat, ApiType, PokemonApiResponse}
import com.google.inject.Inject
import play.api.libs.json.JsObject
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

class PokemonService @Inject()(wsClient: WSClient)(
  implicit ec: ExecutionContext
) {

  private val url: String = "https://pokeapi.co/api/v2/pokemon/"

  def getPokemonByName(name: String): PokemonApiResponse = {
    val urlS = new StringBuilder()
    urlS.append(url)

    if (name != null && name.trim != "") {
      urlS.append("/")
      urlS.append(name)
    } else {
      urlS.append("?offset=20")
      urlS.append("&limit=20")
    }

    val response: Future[WSResponse] = wsClient.url(urlS.toString())
      .withHttpHeaders(("Media-types", MediaTypes.`application/json`.value))
      .get()
    val pokemon = Await.result(response, 10.seconds)

    val pokemonApiResponse: PokemonApiResponse =
      PokemonApiResponse.format.reads(pokemon.json).get

    setWithState(pokemonApiResponse)

    pokemonApiResponse
  }

  def setWithState(pokemon: PokemonApiResponse): Seq[ApiStat] = {
    // Get pokémon types
    val types: Seq[ApiType] = pokemon.types
    val typesUrls =
      types.map(`type` => `type`.`type`).map(t => t.url)

    val pokemonTypesFuture: Seq[Future[WSResponse]] = typesUrls.map((url: URL) => {
        val pokemonType = wsClient.url(url.toString)
          .withHttpHeaders(("Media-types", MediaTypes.`application/json`.value))
          .withQueryStringParameters()
          .get()
        pokemonType
    })

    val pokemonTypes: Seq[ApiType] =
      Await.result(Future.sequence(pokemonTypesFuture), 1.second).flatMap { response =>
        ApiType.format.reads(response.json).asOpt
      }

    // Get all pokémon with the same types
    var pokemons: Seq[PokemonApiResponse] = Seq()
    pokemonTypes.foreach(pokemonType => {
      wsClient.url(s"${url}pokemons?type=${pokemonType.`type`.name}")
        .withHttpHeaders(("Media-types", MediaTypes.`application/json`.value))
        .withQueryStringParameters()
        .get()
        .foreach { r =>
          r.json.validate[Seq[JsObject]].getOrElse(Nil)
            .flatMap(PokemonApiResponse.format.reads(_).asOpt)
            .foreach { pokemonApiResponse =>
              pokemons = pokemons :+ pokemonApiResponse
            }
        }
    })

    // Set average stats for the pokémon
    pokemon.stats.map { pokemonStat =>
      var stats: Seq[ApiStat] = Nil
      pokemons.foreach { pokemonApiResponse =>
        pokemonApiResponse.stats.foreach { stat =>
          if (stat.stat.name == pokemonStat.stat.name) {
            stats = stats :+ stat
          }
        }
      }
      if (stats.size != 0) {
        val averageStat = stats.map(_.base_stat).sum
        pokemonStat.copy(averageStat = averageStat)
      } else {
        pokemonStat.copy(averageStat = 0)
      }
    }
  }

}
