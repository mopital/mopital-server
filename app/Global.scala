import java.util.concurrent.TimeUnit

import akka.actor.{Props, Actor}
import play.api.libs.ws.WS
import play.api.mvc.Filter
import play.api.{Application, Logger, GlobalSettings}
import play.libs.Akka
import play.api.Play.current
import play.mvc.Http.RequestHeader
import play.mvc.Result
import ws.service.beaconmanagement.WcfService

import scala.concurrent.duration.Duration
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by ahmetkucuk on 02/03/15.
 */
object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")

//    val wcfService: WcfService = new WcfService();
//    Logger.debug(wcfService.getBasicHttpBindingIWcfService().getUser("Name0.465239541126362").getName().getValue());

//    Akka.system.scheduler.schedule(Duration.apply(30, TimeUnit.SECONDS), Duration.apply(1, TimeUnit.MINUTES), KeepAlive.instance, Ping)
  }


}
object KeepAlive {
  val instance = Akka.system.actorOf(Props[KeepAlive], name = "keep-alive")
}

class KeepAlive extends Actor {
  
  def receive = {
    case Ping =>
      Logger.info("keep-alive-received");
      WS.url("http://mopital.herokuapp.com/api/ping").get().map({
        response =>
          Logger.info(s"[pong-received] ResponseStatus: ${response.status} ResponseBody: ${response.body}")
      }).recover({
        case t: Throwable =>
          Logger.error("[ping-request-failed]", t)
          None
      })
  }
}

case object Ping
