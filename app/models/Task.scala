package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import play.api.libs.json._


case class Task(id:Long, label:String)

object Task {

   //Parseador, dada una fila JDBC crea un valor de Task
   val task = {
      get[Long]("id") ~ 
      get[String]("label") map {
         case id~label => Task(id, label)
      }
   }


   //Devuelve una lista de tareas a partir de la BD
   def all(): List[Task] =DB.withConnection{//helper DB.withconnection crear y connecta la conexion JDBC
      implicit c =>SQL("select * from task").as(task *)//metodo as permite parsear el ResultSet usando el parseador "task *"
   }

   //Crea una tarea en la BD con la columna 'label' con el nombre pasado
   def create(label: String) {
      DB.withConnection{
         implicit c => 
         val query=SQL("insert into task (label) values ({label})").on('label -> label).executeUpdate()
         
      }

   }

   //Borra una tarea de la BD dada por el id
   def delete(id: Long) {
      DB.withConnection{
         implicit c => SQL("delete from task where id = {id}").on('id -> id).executeUpdate()
      }

   }

   //*Ver tarea por Id*
   //Devuelve la tarea parseada si existe y null sino
   def byId(id: Long):Task=DB.withConnection{
      implicit c => 
          val query=SQL("select * from task where id = {id}").on('id -> id)
          task.single(query()) match{//hacemos comprobación por si no hay ninguna fila con ese id
            case Success(t) => t
            case Error(e) => null
          }
      }

    //Función auxiliar para conocer el número de tareas
    def count():Long=DB.withConnection{
      implicit c=>
      SQL("select count(*) from task").as(scalar[Long].single)
    }



/*
val rowOption = SQL("select id from users where username = {username} and password = {password} limit 1")
        .on('username -> username, 'password -> password)
        .apply
        .headOption
    rowOption match {
      case Some(row) => Some(row[Long]("id"))  // the uid
      case None => None
    }*/

}


