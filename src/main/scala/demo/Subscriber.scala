package demo

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import scredis.SubscriberClient

import scala.collection.immutable
import scala.concurrent.duration.DurationLong
import scala.concurrent.{Await, Future}

object Subscriber extends App {

  implicit val actorSystem: ActorSystem = ActorSystem()
  private val actorMaterializerSettings = ActorMaterializerSettings(actorSystem).withInputBuffer(1, 1)

  implicit val mat = ActorMaterializer(actorMaterializerSettings)

  val subscriberClient = SubscriberClient(tcpReceiveBufferSizeHint = 1)

//  new ScredisSubscriber(subscriberClient).run

//  new ScredisSubscriber(subscriberClient).slowConsumer

  val consumer: immutable.Seq[Long] = Await.result(new ScredisSubscriber(subscriberClient).consumer, 30.seconds)

  println(consumer diff consumer.sorted)

  println(consumer == consumer.sorted)

  println(consumer)
  println(consumer.sorted)

}


