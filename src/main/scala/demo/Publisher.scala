package demo

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, DelayOverflowStrategy, ThrottleMode}
import akka.stream.scaladsl.{Sink, Source}
import scredis.Client

import scala.concurrent.duration.DurationInt

object Publisher extends App {
  implicit val actorSystem: ActorSystem = ActorSystem()
  private val settings = ActorMaterializerSettings(actorSystem).withInputBuffer(1, 1)
  implicit val mat = ActorMaterializer()

  val redisClient = Client()

  //  Source.fromIterator(() ⇒ Iterator.from(1))
  //    .take(50000)
  Source(1 to 50000)
    //    .delay(1.millis, DelayOverflowStrategy.backpressure)
    //    .tick(1.seconds, 1.millis, ())
    //  Source.repeat(())
    .mapAsync(1) {
    //     x ⇒ redisClient.publish("scredis-event", System.currentTimeMillis())
    x ⇒
      println(x)
      redisClient.publish("scredis-event", x)
  }
    .throttle(1000, 1.second, 1000, ThrottleMode.shaping)
    .runWith(Sink.ignore)
}
