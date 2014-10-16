package controllers

//librerias
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import play.api.libs.json._
import play.api.libs.functional.syntax._

import play.api.db._
import java.util.{Date}


//Dependencias
import models.Task

//Controlador principal de la aplicación
object Application extends Controller {

  case class TextTask(label:String,dateE:Option[String])//Clase auxiliar para contener los parámetros recibidos en el POST
  

  //Declaración del form que utilizamos, sirve para realizar la validación
  val taskForm = Form(
    mapping(
      "label" -> nonEmptyText,
      "dateE" -> optional(text)
    )
    (TextTask.apply)(TextTask.unapply)
  )

  def index = Action {
    Redirect("/tasks") //Redirecciona con código 303
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
      errors => BadRequest(errors.errorsAsJson),//si hay errores se recarga la página con código 400
      
      taskForm => {//utilizamos el formulario para coger los parámetros
        try{
          var label:String=taskForm.label
          var dateE =taskForm.dateE.getOrElse("")

          if(dateE!=""){ 
              val format=new java.text.SimpleDateFormat("dd/MM/yyyy")
              var date=format.parse(dateE)
              Task.create(label,alias,date)//Creamos la nueva tarea
          }
          else{
            Task.create(label,alias,null)//Creamos la nueva tarea
          }
          
          val json=Json.toJson(label)// Trasformamos a json la descripción para la respuesta
          Created(json)//Devolvemos la descripción con código 201

          /*val url = Task.create(label)
          Created.withHeaders(LOCATION -> "google.com")*/
        }
        catch {//capturamos una posuble excepcion por no existir el usuario u otra causa
          case nf: Exception => NotFound
        }
        
      }
    )
  }

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

  }


  implicit val taskWrites = new Writes[Task]{

      def writes(task: Task) ={
        var showDate=""
        var date=task.dateE.getOrElse("No tiene")
        if(date !="No tiene"){
          showDate=new java.text.SimpleDateFormat("dd/MM/yyyy").format(date)
        }
        else{
          showDate="No tiene"
        }

        JsObject(

          Seq(
              "id" -> JsString(task.id.toString),
              "label" -> JsString(task.label),
              "alias" -> JsString(task.alias),
              "Fecha Fin" -> JsString(showDate)
          )   
        )
      }
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

  //*Action de ver tarea*
  //Devuelve una tarea en formato JSON si existe el id recibido, si no devuelve un 404
  def tasksbyAlias(alias: String) = Action{
    val tareas=Task.allbyAlias(alias)//Obtenemos las tareas de la BD
    val json=Json.toJson(tareas)//Convertimos las tareas a Json
    Ok(json)
  }


  //*Action de ver las tareas caducadas *
  def allExpired =Action{
    val tareas=Task.allExpired()
    val json=Json.toJson(tareas)//Convertimos las tareas a Json
    Ok(json)
  }

  def allBefore(d:Int,m:Int,y:Int)=Action{
    val date=new Date(d+"/"+m+"/"+y)
    val tareas=Task.allBefore(date)
    val json=Json.toJson(tareas)//Convertimos las tareas a Json
    Ok(json)
  }




}


