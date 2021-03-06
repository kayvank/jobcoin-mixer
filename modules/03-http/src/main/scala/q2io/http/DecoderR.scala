package q2io.http

import org.http4s.circe.JsonDecoder
import cats.syntax.all._
import org.http4s._
import org.http4s.circe._
import q2io.core.effects.MonadThrow
import org.http4s.Request
import org.http4s.dsl.Http4sDsl

object DecoderR {
  implicit class RefinedRequestDecoder[F[_]: JsonDecoder: MonadThrow](
      req: Request[F]
  ) extends Http4sDsl[F] {
    def decodeR[A: io.circe.Decoder](f: A => F[Response[F]]): F[Response[F]] =
      req.asJsonDecode[A].attempt.flatMap {
        case Left(e) =>
          Option(e.getCause) match {
            case Some(c) if c.getMessage.startsWith("Predicate") =>
              BadRequest(c.getMessage)
            case _ => UnprocessableEntity()
          }
        case Right(a) => f(a)
      }
  }
}
