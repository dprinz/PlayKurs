package exercises

import java.util.Date
import slick.driver.H2Driver.simple._

case class Sale(price: Double, idCity:Int)

case class City(id: Int, name:String, idRegion:Int)

case class Region(id: Int, name: String)

object Sales  extends Table[Sale]("CREDENTIALS") {
  def price = column[Double]("PRICE")
  def idCity = column[Int]("ID_CITY")
  def * = price ~ idCity  <> (Sale.apply _, Sale.unapply _)
}

 object Cities  extends Table[City]("CITIES") {
  def id = column[Int]("ID")
  def name = column[String]("NAME")
  def idRegion = column[Int]("ID_REGION")
  def * = id ~ name ~ idRegion <> (City.apply _, City.unapply _) 
 }
 
 object Regions  extends Table[Region]("Regions") {
  def id = column[Int]("ID")
  def name = column[String]("NAME")
  
  def * = id ~ name  <> (Region.apply _, Region.unapply _) 
 }
  
object DBTest extends App{

  val query = for {
    (sales, cities) <- Sales innerJoin Cities on (_.idCity === _.id)
    //regions <- Regions if regions.id === cities.idRegion
  }
  yield {(sales , cities )}
  
  val nextQuery = for {
    (s,regions) <- query innerJoin Regions on(_._2.idRegion === _.id)
  }
  yield {
    (s._1,s._2,regions)
  }
  
  val test = nextQuery.groupBy(_._3.name).map {
    case (name, scr) => (name,scr.map(_._1.price).sum)
  }
  
  val test2= query.groupBy(_._2.name).map {
    case (name, sc) => (name,sc.length,sc.map(_._1.price).sum)
  }
  println(test.selectStatement)
  
  println(test2.selectStatement)
  
  //val test = query innerJoin Regions on (_._1.idRegion === _.id)
    
  
  

}