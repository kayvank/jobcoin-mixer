
ThisBuild / scalaVersion := "2.13.3"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "q2io"
ThisBuild / organizationName := "q2io"
lazy val supportedScalaVersions = List("2.12.12", "2.12.11")

lazy val projectSettings = Seq(
  scalafmtOnCompile := true,
  scalacOptions += "-Ymacro-annotations",
  libraryDependencies ++= Seq(
    compilerPlugin(Dependencies.kindProject cross CrossVersion.full),
    compilerPlugin(Dependencies.betterMonadicFor)
  )
)

lazy val root = (project in file("."))
  .aggregate(domain, core, http, bootstrap)
  .settings(
    name := "jobcoin-mixer"
  )

lazy val domain = (project in file("modules/01-domain"))
  .settings(projectSettings: _*)
  .settings(
    libraryDependencies  ++= Dependencies.core ++ Dependencies.test
  )

lazy val core = (project in file("modules/02-core"))
  .settings(projectSettings: _*)
  .settings(
    libraryDependencies  ++= Dependencies.core ++ Dependencies.test
  )
  .dependsOn(
    domain % "compile->compile;test->test"
  )

lazy val http = (project in file("modules/03-http"))
  .settings(projectSettings: _*)
  .settings(
    libraryDependencies  ++= Dependencies.core ++ Dependencies.test ++ Dependencies.api
  )
  .dependsOn(
    domain % "compile->compile;test->test",
    core % "compile->compile;test->test"
  )

lazy val bootstrap = (project in file("modules/04-bootstrap"))
  .settings(projectSettings: _*)
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(DockerPlugin)
  .settings(
    dockerBaseImage := "openjdk11:jdk11u-alpine-nightly-slim",
    dockerExposedPorts ++= Seq(9000),
    makeBatScripts := Seq(),
    dockerUpdateLatest := true,
    libraryDependencies  ++= Dependencies.core ++ Dependencies.test ++ Dependencies.api
  )
  .dependsOn(
    domain % "compile->compile;test->test",
    core % "compile->compile;test->test",
    http % "compile->compile;test->test"
  )
