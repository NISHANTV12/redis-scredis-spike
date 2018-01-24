package demo

import akka.Done
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{Materializer, OverflowStrategy}
import scredis.PubSubMessage.Message
import scredis.SubscriberClient
import scredis.serialization.LongReader

import scala.collection.immutable
import scala.concurrent.Future

class ScredisSubscriber(subscriberClient: SubscriberClient)(implicit mat: Materializer) {

  private val source: Source[Any, Future[Int]] = Source.actorRef(0, OverflowStrategy.dropBuffer).mapMaterializedValue {
    q ⇒
      subscriberClient.subscribe("scredis-event") {
        case x ⇒
          q ! x
      }
  }

  def slowConsumer: Future[Done] =
    source.buffer(1, OverflowStrategy.dropBuffer).async
      .collect {
        case x: Message ⇒
          val l = System.currentTimeMillis() - LongReader.read(x.message)
          Thread.sleep(1000)
          l
      }.runForeach(println)

  def run: Future[Int] = subscriberClient.subscribe("scredis-event") {
        case x: Message ⇒
          println(System.currentTimeMillis() - LongReader.read(x.message))
          Thread.sleep(1000)
      }

  def consumer: Future[immutable.Seq[Long]] =
    source.buffer(1, OverflowStrategy.dropBuffer).async
      .collect {
        case x: Message ⇒
          LongReader.read(x.message)
      }.take(10000)
      .runWith(Sink.seq)
}
