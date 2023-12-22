ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "dnc"
  )

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "os-lib" % "0.9.1"
)

assembly / mainClass := Some("main")