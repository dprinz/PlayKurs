import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "statisfy-github"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    "org.scalatest" % "scalatest_2.10.0" % "2.0.M5" % "test", //TODO: fix scalaVersion
    "com.typesafe" % "slick_2.10.0-RC2" % "0.11.2" //TODO:remove slick
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  ).dependsOn(RootProject(uri("https://github.com/freekh/play-slick.git#INTERNAL_REVIEW_2")))

}
