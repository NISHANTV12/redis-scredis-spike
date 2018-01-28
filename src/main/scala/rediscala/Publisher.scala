package rediscala

import akka.actor.ActorSystem
import redis.RedisClient

object Publisher {

  implicit val actorSystem: ActorSystem = ActorSystem()

  private val publisher = RedisClient()
  (1 to 100).foreach { x ⇒
    publisher.publish("csw-event", x.toString)
  }
}
