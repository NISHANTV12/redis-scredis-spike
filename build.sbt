name := "RedisScredisSpike"

version := "0.1"

scalaVersion := "2.12.4"
val akkaVersion = "2.5.8" //all akka is Apache License 2.0

resolvers += Resolver.bintrayRepo("jastice","maven")

libraryDependencies += "com.github.scredis" %% "scredis" % "2.1.1"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % akkaVersion
