package exercises

import org.scalatest.matchers.MustMatchers
import org.scalatest.OptionValues._
import org.scalatest._
import play.api.test._
import play.api.test.Helpers._
import models._
import slick.driver.H2Driver.simple._
import slick.lifted.DDL
import slick.driver.BasicQueryTemplate

object DBImplicits extends SlickExercise {
  implicit lazy val app = FakeApplication(additionalConfiguration = Helpers.inMemoryDatabase(dbName))
}

/**
 * SLICK EXERCISE 1
 *
 * ----> START HERE
 */
class SlickExercise1Spec extends SlickExercise {
  import DBImplicits._

  //EXERCISE 1.1: give the correct values for ddl so that
  //before and after creates/drops the Credentials table
  val ddl: DDL = Credentials.ddl

  override def beforeEach = {
    DB.withSession { implicit session =>
      ddl.create

      //EXERICSE 1.2: insert the Credentials in SlickExercise.testCredentials
      Credentials.insertAll(testCredentials: _*)
    }
  }

  override def afterEach = {
    DB.withSession { implicit session =>
      ddl.drop
    }
  }

  "By querying the database you" should {
    "create a query that checks for the credentials for fredrik" in {
      DB.withSession { implicit session =>
        //EXERCISE 1.3: make this test pass by filtering out the
        //               Credentials that has the username 'fredrik'
        //HINT:         use Query(Credentials) and === not ==
        val result: List[Credentials] = Query(Credentials).filter(_.username === "fredrik").list

        result must be === testCredentials.filter(_.username == "fredrik")
        //^ do not cheat by touching this condition ;)
      }

    }
    "learn how to sort data" in {
      DB.withSession { implicit session =>
        //EXERCISE 1.4: make this test pass by creating a query that sorts by usernames in a descending order
        val result: List[Credentials] = Query(Credentials).sortBy(_.username.desc).list

        result must be === testCredentials.sortWith(_.username > _.username)
      }
    }
    "learn how to use pagination with Slick" in {
      DB.withSession { implicit session =>
        //EXERCISE 1.5: to make this test pass query get the 4th first name (sorted in an ascending order)
        val result: List[Credentials] = Query(Credentials).take(5).drop(4).list

        result must be === List(testCredentials(4))
      }
    }
    "learn how to update" in {
      DB.withSession { implicit session =>
        //EXERCISE 1.6: fix this test by changing the password for the username 'fredrik' to 'coolio'
        //insert code HERE that updates the password for 'fredrik' to 'coolio'
        Query(Credentials).filter(_.username === "fredrik").map(_.password).update("coolio")
        //then uncomment the lines below:
        val passwordQuery = for {
          c <- Credentials
          if c.username === "fredrik"
        } yield {
          c.password
        }
        passwordQuery.first must be === "coolio"

      }
    }

  }
}

/**
 * **********************************************************\
 * CONGRATS - YOU ARE DONE WITH THE FIRST SLICK EXERCISE!
 *
 * REMEMBER TO READ THE EXERCISE TEXT FOR THE NEXT EXERCISE
 * BEFORE CONTINUING
 * \**********************************************************
 */

/**
 * SLICK EXERCISE 2
 */
class SlickExercise2Spec extends SlickExercise {
  import DBImplicits._

  //EXERCISE 2.1: give the correct values for ddl so that
  //              before and after creates/drops the *ALL* tables
  val ddl: DDL = Credentials.ddl ++ Settings.ddl

  val testSettings = List(
    ("status", 1337, 1, 1),
    ("answer", 42, 1, 2),
    ("port", 8080, 1, 3),
    ("port", 8080, 2, 4) //       key       value  credsId  id
    )

  override def beforeEach = {
    DB.withSession { implicit session =>
      ddl.create

      Credentials.insertAll(testCredentials: _*)

      Settings.insertAll(testSettings: _*)

      //EXERICSE 2.2: insert testCredentials and testSettings here
    }
  }

  override def afterEach = {
    DB.withSession { implicit session =>
      ddl.drop
    }
  }
  "After fixing the Settings table the following test cases" should {
    "teach you how to do auto incrementing" in {
      DB.withSession { implicit session =>
        //EXERCISE 2.3: fix the Credentials and the Settings table so that
        //              it is possible to find the id of the row which was
        //              inserted. Name the method: autoInc

        val credsId = Credentials.autoInc.insert(Credentials("another", "one"))

        credsId must be === 8

        val settingId = Settings.autoInc.insert("torrent", 6969, credsId)

        settingId must be === 5
      }
    }

    "teach you how to do foreign keys in Slick" in {
      DB.withSession { implicit session =>
        //EXERCISE 2.4: a Setting can never exist without corresponding credentials and
        //              should be automatically deleted once the corresponding credentials
        //              are deleted.
        //HINT:         perhaps a foreign key will do the trick?

        /* Comment this in and make these lines of code work: */

        val settingsLength = Query(Settings.length)
        val id = Credentials.autoInc.insert(Credentials("test", "pass2"))
        settingsLength.first must be === testSettings.length
        Settings.autoInc.insert("test key", 42, id)
        Query(Credentials).filter(_.id === id).delete
        settingsLength.first must be === testSettings.length

      }
    }

    "teach you how to do constraints in Slick" in {
      DB.withSession { implicit session =>
        //EXERCISE 2.5: fix the Credentials table so that it is not possible
        //              to insert 2 Credentials with the same name
        /* Comment this in and make these lines of code work: */

        val thrown = intercept[org.h2.jdbc.JdbcSQLException] {
          Credentials.autoInc.insert(testCredentials(0).copy(id = Some(101), password = "somethingelse"))
        }
        thrown.getMessage must include("Unique")

      }
    }

    "teach you about implicit joining" in {
      DB.withSession { implicit session =>
        //EXERCISE 2.6: in this exercise you will query for all Credentials
        //              that has a setting 'port' with the value '8080'
        /* Uncomment this and make this test pass: */

        val credentialsOn8080 =
          for {
            credentials <- Credentials
            setting <- Settings if (credentials.id === setting.credsId
              && setting.key === "port"
              && setting.value === 8080)
          } yield {
            credentials
          }
          
        credentialsOn8080.list.toSet must be === Set(testCredentials(0), testCredentials(1))

      }
    }

    "teach you how to use query templates" in {
      DB.withSession { implicit session =>
        //EXERCISE 2.7: fix the findUsernameById so that the query template
        //              can be used to find a matching username by passing in the id
        /* Uncomment this and fix the code: */
        
          val findUsernameById: BasicQueryTemplate[Int, String] = 
          for {
            id <- Parameters[Int]
            c <- Credentials
          }
          yield {
            c.username
          }

           findUsernameById(1).first must be === testCredentials(0).username
          
      }
    }

    "teach you about inspecting SQL statements that Slick produces" in {
      //EXERCISE 2.8: fill in the selectStatement, updateStatement, deleteStatement and create statements
      //              so that the tests pass
    	val q = Query(Credentials)
      val selectStatement = q.selectStatement
      val updateStatement = q.updateStatement
      val deleteStatement = q.deleteStatement
      val createStatements = Credentials.ddl.createStatements

      selectStatement must include("select")
      updateStatement must include("update")
      deleteStatement must include("delete")
      createStatements.mkString must include("create")
    }
  }
}

trait SlickExercise extends WordSpec with MustMatchers with BeforeAndAfterEach {
  val dbName = "test"
  lazy val testCredentials: Seq[Credentials] = Seq(
    Credentials("fredrik", "h3mm3l1g", Some(1)),
    Credentials("trond", "hemmelig", Some(2)),
    Credentials("phillip", "heimLi.ch", Some(3)),
    Credentials("heiko", "heimlich", Some(4)),
    Credentials("nilanjan", "rahasya", Some(5)),
    Credentials("ryan", "5eCr8", Some(6)),
    Credentials("brendan", "s3cr37", Some(7)))
  lazy val DB = play.api.db.slick.DB(dbName)
}
