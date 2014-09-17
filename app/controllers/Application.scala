package controllers

//librerias
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

//Dependencias
import models.Task

//Controlador principal de la aplicación
object Application extends Controller {

  //Declaración del form que utilizamos, sirve para realizar la validación
  val taskForm= Form(
    "label" -> nonEmptyText
  )

  def index = Action {
    Redirect(routes.Application.tasks) //Redirecciona con código 303
  }

  

  //Tareas
  def tasks = Action {
    //Ok("Bien")
    Ok(views.html.index(Task.all(), taskForm))
    
  }


  //Constructor
  def newTask = TODO//TODO:cÓDIGO 501


  //Borrado
  def deleteTask(id: Long) = TODO


}


