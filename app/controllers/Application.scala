package controllers

//librerias
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import play.api.libs.json._
import play.api.libs.functional.syntax._

import play.api.db._


//Dependencias
import models.Task

//Controlador principal de la aplicación
object Application extends Controller {

  //Declaración del form que utilizamos, sirve para realizar la validación
  val taskForm = Form(
    //mapping(
      "label" -> nonEmptyText//,
      //"alias" -> default(text,"Anonymous")
    //)
    //((label,user) => Task(0,label,user))
    //(Task.apply)(Task.unapply)
  )

  def index = Action {
    Redirect(routes.Application.tasks) //Redirecciona con código 303
  }

  

  //*Lista de Tareas*
  def tasks = Action {
    val tareas=Task.all()//Obtenemos las tareas de la BD
    val json=Json.toJson(tareas)//Convertimos las tareas a Json
    Ok(json)
  }


  //*Action de crear tarea*
  def newTask(alias: String) = Action{
    implicit request => taskForm.bindFromRequest.fold(//peticion interna de la página
      errors => BadRequest(views.html.index(Task.all(),errors)),//si hay errores se recarga la página con código 400
      label => {
        
        try{
          Task.create(label,alias)//Creamos la nueva tarea
          
          val json=Json.toJson(label)// Trasformamos a json la descripción para la respuesta
          Created(json)//Devolvemos la descripción con código 201

          /*val url = Task.create(label)
          Created.withHeaders(LOCATION -> "google.com")*/
        }
        catch{//capturamos si se pruduce excepcion en la BD por no existir el usuario
          case nf:Exception => NotFound
        }
      }
    )
  }


//@Util
//@Catch(value =Exception.class)
  
  
  //*Action de borrar tarea* 
  //Invoca al método de borrar tarea y redirecciona
  def deleteTask(id: Long) = Action{  
    val t=Task.byId(id)
    if(t!=null){
      Task.delete(id)
      Ok
    }
    else{
      NotFound
    }

    /*try{
      Task.delete(id)
      Ok
    }
    catch{
       case _ => NotFound//NotFound

    }*/
  }


  implicit val taskWrites = new Writes[Task]{
      def writes(task: Task) = Json.obj(
         "label" -> task.label,
         "id" -> task.id,
         "alias" -> task.alias
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


