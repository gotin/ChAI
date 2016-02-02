name := "ChAIChAI"

version := "1.0"

scalaVersion := "2.11.7"

resolvers += "Atilika Open Source repository" at "http://www.atilika.org/nexus/content/repositories/atilika"

autoCompilerPlugins := true

// addCompilerPlugin("org.scala-lang.plugins" %% "scala-continuations-plugin_2.11.7" % "1.0.2")

// scalacOptions += "-P:continuations:enable"

// Add dependency on ScalaFX library
libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.60-R9"
libraryDependencies += "org.jsoup" % "jsoup" % "1.8.3"
libraryDependencies += "org.atilika.kuromoji" % "kuromoji" % "0.7.7"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.3",
  "org.scala-lang.modules" %% "scala-xml" %  "1.0.5",
  "org.scala-lang.plugins" %% "scala-continuations-library" % "1.0.2"
)

// Add dependency on JavaFX library based on JAVA_HOME variable
unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/jfxrt.jar"))

