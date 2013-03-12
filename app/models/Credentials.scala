package models

import slick.driver.H2Driver.simple._

/** Credentials defines username and password for a user
  * @param id it will be useful when we start persisting to the DB
  */
case class Credentials(username: String, password: String, id: Option[Int] = None)


object Credentials extends Table[Credentials]("CREDENTIALS") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def username = column[String]("USERNAME", O.NotNull)
  def password = column[String]("PASSWORD", O.NotNull) 
  def * = username ~ password ~ id.?  <> (Credentials.apply _, Credentials.unapply _)
  def autoInc = * returning id
  def uniqueIndex = index("INDEX_NAME", username, unique = true)
  // rows ^    ^      ^   are bound to User
  // notice: ~ seperates a column from another
  // notice: the order has to match the (un)apply methods
}