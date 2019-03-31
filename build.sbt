import sbtassembly.AssemblyPlugin._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "hu.andrasszatmari"
ThisBuild / organizationName := "andrasszatmari"

lazy val root = (project in file("."))
  .settings(
    name := "Goose Game",

    libraryDependencies += "org.tpolecat" %% "atto-core" % "0.6.5",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test,

    mainClass in assembly := Some("hu.andrasszatmari.goosegame.Main"),

    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x => MergeStrategy.first
    },

    assemblyOutputPath in assembly := baseDirectory.value / "bin" / "goose-game.jar",

    assemblyJarName in assembly := s"${name.value}",

    assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(defaultUniversalScript(shebang = false))),
  )
