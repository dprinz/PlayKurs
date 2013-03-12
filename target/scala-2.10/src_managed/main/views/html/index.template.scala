
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
object index extends BaseScalaTemplate[play.api.templates.Html,Format[play.api.templates.Html]](play.api.templates.HtmlFormat) with play.api.templates.Template1[List[models.github.Repo],play.api.templates.Html] {

    /**/
    def apply/*1.2*/(repos: List[models.github.Repo]):play.api.templates.Html = {
        _display_ {

Seq[Any](format.raw/*1.35*/("""

"""),_display_(Seq[Any](/*3.2*/main("Welcome to Play 2.1")/*3.29*/ {_display_(Seq[Any](format.raw/*3.31*/("""
    
    """),_display_(Seq[Any](/*5.6*/for(repo <-repos) yield /*5.23*/ {_display_(Seq[Any](format.raw/*5.25*/("""
    	"""),_display_(Seq[Any](/*6.7*/repo/*6.11*/.name)),format.raw/*6.16*/("""
    """)))})),format.raw/*7.6*/("""
    <a href=""""),_display_(Seq[Any](/*8.15*/routes/*8.21*/.Application.logout)),format.raw/*8.40*/("""">Logout</a>
    
""")))})),format.raw/*10.2*/("""
"""))}
    }
    
    def render(repos:List[models.github.Repo]): play.api.templates.Html = apply(repos)
    
    def f:((List[models.github.Repo]) => play.api.templates.Html) = (repos) => apply(repos)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Tue Mar 12 15:01:12 CET 2013
                    SOURCE: /home/dprinz/Repositories/statisfy-github/app/views/index.scala.html
                    HASH: 1024f85d50e53dd97c640b84a033c4082412df65
                    MATRIX: 523->1|633->34|670->37|705->64|744->66|789->77|821->94|860->96|901->103|913->107|939->112|975->118|1025->133|1039->139|1079->158|1129->177
                    LINES: 19->1|22->1|24->3|24->3|24->3|26->5|26->5|26->5|27->6|27->6|27->6|28->7|29->8|29->8|29->8|31->10
                    -- GENERATED --
                */
            