package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import play.api.libs.json._

import java.util.{Date}

case class TextTask(label:String,dateE:Option[String])

case class Task(id:Long, label:String,alias: String,dateE:Option[Date])

object Task {

    //Parseador, dada una fila JDBC crea un valor de Task
    val task = {
      get[Long]("id") ~ 
      get[String]("label") ~
      get[String]("alias") ~
      get[Option[Date]]("dateE") map {
         case id~label~alias~dateE => Task(id, label,alias,dateE)
      }
    }

    
    //Devuelve una lista de tareas a partir de la BD
    def all(): List[Task] =DB.withConnection{//helper DB.withconnection crear y connecta la conexion JDBC
      implicit c =>SQL("select * from task").as(task *)//metodo as permite parsear el ResultSet usando el parseador "task *"
    }

    //Crea una tarea en la BD con la columna 'label' con el nombre pasado y el usuario asignado
    def create(label: String, alias: String,dateE:Date) {
      DB.withConnection{
          implicit c =>

          if(dateE!=null){
            val query=SQL("insert into task (label,alias,dateE) values ({label},{alias},{dateE})").on('label -> label, 'alias -> alias,'dateE -> dateE).executeUpdate()
          }
          else{
            val query=SQL("insert into task (label,alias) values ({label},{alias})").on('label -> label, 'alias -> alias).executeUpdate()
         }

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

    //*Ver tareas por Alias*
    // Devuelve todas las tareas del usuario especificado por alias
    def allbyAlias(alias: String): List[Task] =DB.withConnection{//helper DB.withconnection crear y connecta la conexion JDBC
      implicit c =>SQL("select * from task where alias = {alias}").on('alias -> alias).as(task *)//metodo as permite parsear el ResultSet usando el parseador "task *"
    }



    //Función auxiliar para conocer el número de tareas
    def count():Long=DB.withConnection{
      implicit c=>
      SQL("select count(*) from task").as(scalar[Long].single)
    }


    def allExpired():List[Task]=DB.withConnection{//helper DB.withconnection crear y connecta la conexion JDBC
      var today=new Date

      implicit c =>SQL("select * from task where dateE < {today} ").on('today -> today).as(task *)//metodo as permite parsear el ResultSet usando el parseador "task *"
    }

    def allBefore(date:Date):List[Task]=DB.withConnection{
      implicit c =>SQL("select * from task where dateE < {date} ").on('date -> date).as(task *)//metodo as permite parsear el ResultSet usando el parseador "task *"
    }

    def changeDate(id:Long,date:Date)=DB.withConnection { 
        implicit connection =>
          SQL(""" 
                UPDATE Task SET
                dateE = {dateE}
                WHERE id = {id}
          """).on(
              'id -> id,
              'dateE -> date
          ).executeUpdate

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


