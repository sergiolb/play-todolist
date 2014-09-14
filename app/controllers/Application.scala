package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    //Ok(views.html.index("Your new application is ready."))
    Redirect(routes.Application.tasks)//Redirecciona con código 303
  }

  //Tareas
  def tasks = TODO //TODO:cÓDIGO 501

  //Constructor
  def newTask = TODO

  //Borrado
  def deleteTask(id: Long) = TODO


}