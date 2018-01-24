package demo

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import scredis.Client

import scala.concurrent.duration.DurationInt

object Publisher extends App {
    implicit val actorSystem: ActorSystem = ActorSystem()
    implicit val mat = ActorMaterializer()

    val redisClient = Client()

  Source.tick(1.seconds, 1.millis, ())
//  Source.repeat(())
    .mapAsync(1) {
     x ⇒ redisClient.publish("scredis-event", System.currentTimeMillis())
  }.runWith(Sink.ignore)
}
