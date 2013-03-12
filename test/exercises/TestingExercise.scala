package exercises

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import org.scalatest.OptionValues._
import play.api.test._
import play.api.test.Helpers._
import play.api.mvc.Result
import controllers.Application

class TestingExerciseSpec extends WordSpec with MustMatchers {

  "This exercise" should {

    "teach you how to test that templates are rendered correctly" in {
      //EXERCISE 1.1: Make the tests below pass, by rendering the login template
      //              then extract the content type and the content it self      
      val result = views.html.login(Application.userForm)
      val extractedContentType = contentType(result)
      val extractedContent = contentAsString(result)

      extractedContentType must be === "text/html"
      extractedContent must include("Username")
    }

    "teach you about running applications" in {
      //EXERCISE 1.2: while running a FakeApplication
      //              use a FakeRequest to check if login works
      //              by filling in the extractedStatus and
      //              content       
      implicit val app = FakeApplication()
      running(FakeApplication()) { //OUT
        val result = route(FakeRequest(POST, "/login").withFormUrlEncodedBody("username"->"fredrik","password"->"cool")).get
        val extractedStatus = status(result)
        val extractedContent = contentAsString(result)
        extractedStatus must be === OK
        extractedContent must include("fredrik was posted")

      }

    }

    "teach you about running Test" in {
      //EXERCISE 1.2: run a TestServer using HTMLUNIT and 
      //              go to the login page and check that the 
      //              page source includes 'Username'      
      running(TestServer(3333), HTMLUNIT) { browser =>
        //                      ^ or FIREFOX
        browser.goTo("http://localhost:3333/login")

        val pageSource = browser.pageSource

        pageSource must include("Username")
      }

    }
  }
}
