ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.1"

lazy val root = (project in file("."))
  .settings(
    name := "untitled",
    idePackagePrefix := Some("lev.test")
  )

libraryDependencies += "org.typelevel" %% "cats-effect" % "3.4.8"