package controllers

//librerias
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import play.api.libs.json._
import play.api.libs.functional.syntax._


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
        Task.create(label)//Creamos la nueva tarea
        val json=Json.toJson(label)// Trasformamos a json la descripción para dla respuesta
        Created(json)//Devolvemos la descripción con código 201
      }
    )
  }


  //*Action de borrar tarea* 
  //Invoca al método de borrar tarea y redirecciona
  def deleteTask(id: Long) = Action{
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }


  implicit val taskWrites = new Writes[Task]{
      def writes(task: Task) = Json.obj(
         "label" -> task.label,
         "id" -> task.id 
      )
  }

  //*Action de ver tarea*
  //Devuelve una tarea en formato JSON si existe el id recibido, si no devuelve un 404
  def task(id: Long) = Action{
      val t=Task.byId(id)//extraemos la tarea de BD, devuelve null sino

      if(t!= null){
        val json= Json.toJson(t)
        Ok(json)
      }
      else {
        NotFound
      }
  }

}


