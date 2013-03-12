
package views.html

import play.templates._
import play.templates.TemplateMagic._

import play.api.templates._
import play.api.templates.PlayMagic._
import models._
import controllers._
import play.api.i18n._
import play.api.mvc._
import play.api.data._
import views.html._
/**/
object login extends BaseScalaTemplate[play.api.templates.Html,Format[play.api.templates.Html]](play.api.templates.HtmlFormat) with play.api.templates.Template1[Form[Credentials],play.api.templates.Html] {

    /**/
    def apply/*1.2*/(loginForm: Form[Credentials]):play.api.templates.Html = {
        _display_ {import helper._

import helper.twitterBootstrap._


Seq[Any](format.raw/*1.32*/("""
"""),format.raw/*4.1*/("""<html>
<head>
	<title>Statisfy Login</title>
</head>
<body>
"""),_display_(Seq[Any](/*9.2*/{ //How to handle global errors
  loginForm.globalError.map{ error =>
    <span class="error">{error.message}</span>
  }
})),format.raw/*13.2*/("""
"""),_display_(Seq[Any](/*14.2*/form(routes.Application.submit)/*14.33*/ {_display_(Seq[Any](format.raw/*14.35*/("""
	"""),_display_(Seq[Any](/*15.3*/inputText(loginForm("username"), args = 'placeholder -> "Username", '_label -> "", '_showConstraints -> false))),format.raw/*15.113*/("""
	"""),_display_(Seq[Any](/*16.3*/inputPassword(loginForm("password"), args = 'placeholder -> "Password", '_label -> "", '_showConstraints -> false))),format.raw/*16.117*/("""
	<input type="submit"/>
""")))})),format.raw/*18.2*/("""

<a href=""""),_display_(Seq[Any](/*20.11*/routes/*20.17*/.Application.index())),format.raw/*20.37*/("""">Home</a>
"""),format.raw/*28.10*/("""
</body>
</html>"""))}
    }
    
    def render(loginForm:Form[Credentials]): play.api.templates.Html = apply(loginForm)
    
    def f:((Form[Credentials]) => play.api.templates.Html) = (loginForm) => apply(loginForm)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Mon Mar 11 12:59:35 CET 2013
                    SOURCE: /home/dprinz/Repositories/statisfy-github/app/views/login.scala.html
                    HASH: 9013264a1391cf28c5714b3384362a309cef0c10
                    MATRIX: 516->1|673->31|700->83|795->144|938->266|975->268|1015->299|1055->301|1093->304|1226->414|1264->417|1401->531|1458->557|1506->569|1521->575|1563->595|1602->944
                    LINES: 19->1|25->1|26->4|31->9|35->13|36->14|36->14|36->14|37->15|37->15|38->16|38->16|40->18|42->20|42->20|42->20|43->28
                    -- GENERATED --
                */
            