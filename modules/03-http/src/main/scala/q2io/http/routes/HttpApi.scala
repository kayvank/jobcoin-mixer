package q2io.http.routes

import cats.effect._
import cats.syntax.all._
import org.http4s._
import org.http4s.implicits._
import org.http4s.server.middleware._
import scala.concurrent.duration._

object HttpApi {
  def apply[F[_]: Concurrent: Timer] =
    Sync[F].delay(new HttpApi[F])
}
final class HttpApi[F[_]: Concurrent: Timer] {

  private val addressRoutes: HttpRoutes[F] =
    new AddressRoutes[F].routes
  private val orderRoutes: HttpRoutes[F] = new OrderRoutes[F].routes
  private val healthCheck: HttpRoutes[F] =
    new HealthCheck[F]("ok").routes

  private val logger: HttpApp[F] => HttpApp[F] = {
    { http: HttpApp[F] => RequestLogger.httpApp(true, true)(http) } andThen {
      http: HttpApp[F] => ResponseLogger.httpApp(true, true)(http)
    }
  }

  val publicRoutes: HttpRoutes[F] =
    addressRoutes <+> healthCheck <+> orderRoutes

  private val middleware: HttpRoutes[F] => HttpRoutes[F] = {
    { http: HttpRoutes[F] =>
      AutoSlash(http)
    } andThen { http: HttpRoutes[F] =>
      CORS(http, CORS.DefaultCORSConfig)

    } andThen { http: HttpRoutes[F] => Timeout(60.seconds)(http) }
  }
  val httpApp: HttpApp[F] = logger(middleware(publicRoutes).orNotFound)

}
