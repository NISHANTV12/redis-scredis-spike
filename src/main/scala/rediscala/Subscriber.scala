package rediscala

import java.net.InetSocketAddress

import akka.actor.{ActorSystem, Props}
import akka.util.ByteString
import redis.actors.RedisSubscriberActor
import redis.api.pubsub.{Message, PMessage}

object Subscriber extends App {

  implicit val actorSystem: ActorSystem = ActorSystem()

  actorSystem.actorOf(
    Props(new SubscribeActor(Seq("csw-event"), Seq("pattern.*")))
      .withDispatcher("rediscala.rediscala-client-worker-dispatcher")
  )
}

class SubscribeActor(channels: Seq[String] = Nil, patterns: Seq[String] = Nil)
    extends RedisSubscriberActor(
      new InetSocketAddress("localhost", 6379),
      channels,
      patterns,
      onConnectStatus = connected ⇒ { println(connected) }
    ) {
  override val address: InetSocketAddress = new InetSocketAddress("localhost", 6379)

  def onMessage(message: Message) {
    ByteString
    println(s"message received: ${message.data.decodeString(ByteString.UTF_8)}")
  }

  def onPMessage(pmessage: PMessage) {
    println(s"pattern message received: $pmessage")
  }
}
