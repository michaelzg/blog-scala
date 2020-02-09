lazy val commonSettings = Seq(
  scalaVersion := "2.12.8",
  scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation"),
  cancelable in Global := true)

lazy val akkaVersion = "2.6.3"
lazy val commonDependencies = libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "joda-time" % "joda-time" % "2.10.5",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.0.7" % Test)

lazy val fsm = (project in file("cooking-fsm-demo")).settings(
  name := "cooking-fsm-demo",
  version := "1.0",
  commonSettings,
  commonDependencies)

lazy val uhnd = (project in file("unified-historical-new-datastream")).settings(
  name := "unified-historical-new-datastream",
  version := "1.0",
  commonSettings,
  commonDependencies)
