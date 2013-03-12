package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.data.validation.Constraints._
import models.Credentials
import slick.driver.H2Driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import play.api.mvc.Security._
import play.api.libs.ws.WS
import scala.concurrent.Await
import scala.concurrent.duration._
import play.api.Play.current
import play.api.cache.Cache
import models.github.Repo

object Application extends Controller {

  val userForm = Form(
    mapping(
      "username" -> nonEmptyText(minLength = 4),
      "password" -> nonEmptyText(minLength = 4)) { (name, password) => Credentials(name, password) } { credentials => Some(credentials.username -> credentials.password) }
      verifying ("badusername/password",
        credentials => {
          Cache.getAs[String](credentials.username) match {
            case Some(password) => password == credentials.password
            case None =>
              {
                val request = WS.url("https://api.github.com/user")
                  .withAuth(credentials.username, credentials.password, com.ning.http.client.Realm.AuthScheme.BASIC)
                  .withTimeout(5000).get
                val response = Await.result(request, 5 seconds)
                Cache.set(credentials.username, credentials.password)
                response.status == OK
              }
          }
        }))

  def index = Action { request =>

    (for {
      username <- request.session.get("username")
      password <- Cache.getAs[String](username)
    } yield {
      println(username)
      val query = WS.url("https://api.github.com/user/repos")
        .withAuth(username, password, com.ning.http.client.Realm.AuthScheme.BASIC)
        .get
      val response = Await.result(query, 5 seconds)
      println(response.status)
      println(response.statusText)
      println(response.body)
      val json = response.json
      println(json)
      val repos: List[Repo] = json.as[Seq[Repo]].toList
      Ok(views.html.index(repos))
    }).getOrElse(Ok(views.html.index(Nil)))

  }

  def login = Action {
    Ok(views.html.login(userForm))
  }

  def submit = Action { implicit request =>
    userForm.bindFromRequest().fold(
      error => BadRequest(views.html.login(error)),
      form => Results.Redirect(routes.Application.index).withSession("username" -> form.username))
  }

  def logout = Action {
    Ok(views.html.login(userForm)).withNewSession
  }

}