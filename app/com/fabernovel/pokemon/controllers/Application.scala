package com.fabernovel.pokemon.controllers

import com.fabernovel.pokemon.views._
import javax.inject.Inject
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}


class Application @Inject()(
  controllerComponents: ControllerComponents
) extends AbstractController(controllerComponents) {

  def index: Action[AnyContent] = Action {
    Ok(html.index("Your new application is ready."))
  }

}
