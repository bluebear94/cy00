name := "Cy00"

version := "0.0.0.0"

scalaVersion := "2.12.0-M3"

resolvers += Resolver.sonatypeRepo("public")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.scalactic" % "scalactic_2.11" % "2.2.6",
  "org.scalatest" % "scalatest_2.11" % "2.2.6" % "test",
  "org.scala-lang.modules" %% "scala-swing" % "2.0.0-M2",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
  "com.github.scopt" % "scopt_2.11" % "3.4.0"
)
