package q2io.http.routes

import cats._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

import q2io.core.effects._
import q2io.domain.model._
import q2io.domain.protocol.JsonCodecs._

final class HealthCheck[F[_]: Defer: JsonDecoder: MonadThrow](parm: String)
    extends Http4sDsl[F] {
  private val prefixPath: String = "/v1"
  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "healthcheck" =>
      Ok(HealthCheck(parm))
  }
  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)
}
