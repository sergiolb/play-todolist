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


  //*Action de crear tarea*
  def newTask = Action{
    implicit request => taskForm.bindFromRequest.fold(//peticion interna de la página
      errors => BadRequest(views.html.index(Task.all(),errors)),//si hay errores se recarga la página con código 400
      label => {
        Task.create(label)
        Redirect(routes.Application.tasks)//Redirigimos a la vista para ver todas las tareas
      }
    )
  }


  //*Action de borrar tarea* 
  //Invoca al método de borrar tarea y redirecciona
  def deleteTask(id: Long) = Action{
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }

}


