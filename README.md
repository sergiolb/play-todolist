Play-TodoList
=========

PlayTodoList es una aplicación REST en Scala que te ayuda a organizarte creando tareas

  - Permite crear un número ilimitado de tareas y asignarlas a un usuario
  - Opcionalmente, las tareas pueden tener una fecha límite
  - Puede verse en cualquier momento todas las tareas que caducan antes de una fecha determinada



#Versión
-----------

1.0 -Primera versión con funcionalidades básicas

#Tecnología utilizada
-----------

El proyecto utiliza

* [Scala]  
* [PlayFrameWork] - the High VelocityWeb Framework For Java and Scala
* [Anorm] - BD


#Instalación
--------------

```sh
git clone [git-repo-url] play-todolist
cd play-todolist
activator run
```
Inicializar BD desde [localhost:9000]

#### Puede probarse desde [Heroku]. ####


# Introducción
---------------

La aplicación se basa en tareas. Cada tarea está identificada con un **id** y pertenece a un **usuario**. Además contiene una **etiqueta** que indica su propósito y, opcionalmente, una **fecha** límite.


```object
Task={
    id: "Long" not null,
    label: "String",
    alias: "String",
    dateE: "Date"
}
```

Los usuarios están ya creados y les son asignadas tareas. Si al crear una tarea no se asigna a ningún usuario, esta pasa ser del usuario 'Anónimo'

```object
TaskUser={
    alias: "String" not null,
    name: "String",
}
```


# Distribución#

Hay tres grupos de ficheros importantes,**configuración(Routes y x.sql)**,**Controladores** y **modelos**.


##Routes##
Define todos los tipos de peticiones que tiene nuestra Aplicación y por tanto se encarga de dar acceso a nuestro API

##X.sql
Definen nuestra BD creada en **Anorm**. Se ejecutan uno detrás del otro una vez se lo indiquemos al arrancar la aplicación.

##Controladores

Contienen las funciones que se invocan al hacer peticiones al API. Cada acción devuelve un *código de estado* y en ocasiones un *mensaje* en la respuesta

####Application.scala
Se encarga de todas las peticiones


##Modelos
Contienen la estructura de los objetos que usa nuestra aplicación y se encarga del acceso a la BD.También contiene todas las funciones auxiliares y parseadores necesarios para poder media entre la BD y los controladores.

# API#

Podemos dividir las peticiones que recibe nuestro API en

## Constructores

Se utilizan para generar las entidades que forman nuestra aplicación.

```
POST  /tasks
```
Crea una tarea asignándole como usuario "Anónimo".

Parámetros: 
* label -> Nombre de la tarea
* DateE(Opcional) -> Fecha de finalización de la tarea

Códigos: 
* 201 con json del label de la nueva tarea : Si la tarea se ha creado correctamente
* 404 : Si algo falla
        
--------------------------------------------------------
```
POST  /users/:alias/tasks
```
Crea una tarea asignándole como usuario el indicado.

Parámetros:
* label -> Nombre de la tarea
* DateE(Opcional) -> Fecha de finalización de la tarea

Códigos: 
* 201 con json del label de la nueva tarea : Si la tarea se ha creado correctamente
* 404 : Si algo falla


##Modificadores
Se utilizan para modificar entidades que ya existen

```
PUT /tasks/:id
```
Modifica la fecha de la tarea asignada.

Parámetros: 
* label -> Nombre de la tarea
* DateE -> Fecha de finalización de la tarea

Códigos: 
* 200 : Si la tarea se ha modificado correctamente
* 304 : Si la tarea no se ha modificado 

##Visualizadores
Consulta de tareas en base a filtros

```
GET   /tasks 
```
Obtiene en Json todas las tareas del usuario "Anónimo"

Parámetros: 

Códigos: 
* 200 con Json de la lista de tareas
_____________________________________
```
GET /tasks/:id  
```
Obtiene en Json todos los datos de la tarea seleccionada

Parámetros: 

Códigos: 
* 200 con Json con los datos de la tarea
* 404 si la tarea no existe
_____________________________________
```
GET /users/:alias/tasks 
```
Obtiene en Json todas las tareas del usuario especificado

Parámetros: 

Códigos: 
* 200 con Json con los datos de la tarea
_____________________________________
```
GET /tasks/expired   
```
Obtiene en Json todas las tareas que estén caducadas

Parámetros: 
* label -> Nombre de la tarea
* DateE -> Fecha de finalización de la tarea

Códigos: 
* 200 con Json de la lista de tareas que cumplen la condición

_________________________________________________________________
```
GET /tasks/before/:d-:m-:y   
```
Obtiene en Json todas las tareas que caduquen antes de la fecha especificada.

Parámetros: 
* label -> Nombre de la tarea
* DateE -> Fecha de finalización de la tarea

Códigos: 
* 200 con Json de la lista de tareas que cumplen la condición


##Borradores
Se utilizan para borrar entidades que ya existen

```
DELETE  /tasks/:id 
```
Borra la tarea asignada.

Parámetros: DateE -> Fecha de finalización de la tarea

Códigos: 
* 200 : Si la tarea se ha borrado correctamente
* 404 : Si la tarea no se ha borrado

#Implementación
---------------
En la creación de tarea formateamos la fecha a partir del String recibido

```scala
//*Action de crear tarea*
  def newTask(alias: String) = Action{
    
    implicit request => taskForm.bindFromRequest.fold(//peticion interna de la página
      errors => BadRequest(errors.errorsAsJson),//si hay errores se recarga la página con código 400
      
      taskForm => {//utilizamos el formulario para coger los parámetros
        try{
          var label:String=taskForm.label
          var dateE =taskForm.dateE.getOrElse("")

          if(dateE!=""){ //Si contiene fecha
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
```





Licencia
----

OpenSource


**Free Software, Hell Yeah!**

[Scala]:http://www.scala-lang.org/
[PlayFrameWork]:https://www.playframework.com/
[Anorm]:https://www.playframework.com/documentation/2.1.1/ScalaAnorm
[Heroku]:http://secure-ridge-6406.herokuapp.com/
[localhost:9000]:http://localhost:9000

