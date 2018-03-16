name := """canter-product-server"""
organization := "com.canter"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies += guice
libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.3"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.1"

libraryDependencies ++= Seq(evolutions)
//libraryDependencies += "com.h2database" % "h2" % "1.4.196"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.canter.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.canter.binders._"
