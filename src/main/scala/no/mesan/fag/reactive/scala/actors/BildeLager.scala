package no.mesan.fag.reactive.scala.actors

import akka.actor.{ActorLogging, Props, ActorRef, Actor}
import no.mesan.fag.reactive.scala.actors.BildeAnsvarlig.FoundImage
import no.mesan.fag.reactive.scala.pictures.ThumbnailCreator

/** Lager thumbnails av bilder, sender svar tilbake til klientansvarlig. */
class BildeLager(klientAnsvarlig: ActorRef) extends Actor with ActorLogging with Tracer {
  import BildeLager.ImageResult
  traceCreate(s"BildeLager hos $klientAnsvarlig")

  override def receive = {
    case image: FoundImage =>
      image.traceReceive(this)
      val url = image.url
      klientAnsvarlig ! ImageResult(url, ThumbnailCreator.create(url))
      context stop self
  }
}

/** Opprettelse og meldinger. */
object BildeLager {

  /** BildeURL+thumbnail */
  case class ImageResult(url: String, thumbnail: String) extends ApplicationMessage

  /** Props for ny forekomst. */
  def props(klientAnsvarlig: ActorRef) = Props(classOf[BildeLager], klientAnsvarlig)
}
