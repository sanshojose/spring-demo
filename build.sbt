name := "spring-demo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies ++= Seq(
  javaCore,
  javaWs,
  "com.typesafe.akka" %% "akka-camel" % "2.3.4",
  "org.apache.camel" % "camel-amqp" % "2.10.3",
  "org.apache.activemq" % "activemq-camel" % "5.8.0",
  "javax.json" % "javax.json-api" % "1.0",
  "org.glassfish" % "javax.json" % "1.0.4",
  "org.springframework" % "spring-context" % "4.1.1.RELEASE",
  "org.springframework" % "spring-orm" % "4.1.1.RELEASE",
  "org.springframework" % "spring-jdbc" % "4.1.1.RELEASE",
  "org.springframework" % "spring-tx" % "4.1.1.RELEASE",
  "org.springframework" % "spring-expression" % "4.1.1.RELEASE",
  "org.springframework" % "spring-aop" % "4.1.1.RELEASE",
  "org.springframework" % "spring-test" % "4.1.1.RELEASE" % "test"
)

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.11" % "test",
  "com.novocode" % "junit-interface" % "0.9" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.4" % "test" exclude("javax.ws.rs", "jsr311-api")
)

