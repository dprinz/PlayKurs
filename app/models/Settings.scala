package models

import slick.lifted.ForeignKeyAction //hint
import slick.driver.H2Driver.simple._

/* This code will be used in the last Slick exercise*/
object Settings extends Table[(String, Int, Int, Int)]("SETTINGS") {

  def id = column[Int]("SETTINGS_ID", O.PrimaryKey, O.AutoInc)
  def key = column[String]("SETTINGS_KEY", O.NotNull)
  def value = column[Int]("SETTINGS_VALUE")
  def credsId = column[Int]("SETTINGS_CREDS_ID")
  
  def * = key ~ value ~ credsId ~ id
  
  def autoInc =  key ~ value ~ credsId returning id
  
  def credentials = foreignKey("FK_CRED", credsId, Credentials)(_.id,
    onDelete = ForeignKeyAction.Cascade,
    onUpdate = ForeignKeyAction.Cascade)
    
}

