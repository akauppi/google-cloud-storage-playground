import sbt.Keys.libraryDependencies

name := "google-cloud-storage-playground"

scalaVersion := "2.12.6"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "utf8",
  "-feature",
  "-unchecked",
  "-language", "postfixOps"
)

//libraryDependencies += "com.typesafe" % "config" % "1.3.1"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

//libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies ++= {
  val ver = "1.35.0"
  Seq(
    "com.google.cloud" % "google-cloud-storage" % ver,
    "com.google.cloud" % "google-cloud-pubsub" % ver
  )
}
