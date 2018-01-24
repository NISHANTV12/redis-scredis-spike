package demo

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import scredis.SubscriberClient

object Subscriber extends App {

  implicit val actorSystem: ActorSystem = ActorSystem()
  private val actorMaterializerSettings = ActorMaterializerSettings(actorSystem).withInputBuffer(1, 1)

  implicit val mat = ActorMaterializer(actorMaterializerSettings)

  val subscriberClient = SubscriberClient(tcpReceiveBufferSizeHint = 1)

  new ScredisSubscriber(subscriberClient).run

//  new ScredisSubscriber(subscriberClient).slowConsumer

}


