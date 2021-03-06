package q2io.bootstrap

import cats.effect._
import org.http4s.server.blaze.BlazeServerBuilder
import scala.concurrent.ExecutionContext

import q2io.http.routes.HttpApi

object Mian extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    for {
      api <- HttpApi[IO]
      _ <- BlazeServerBuilder[IO](ExecutionContext.global)
        .bindHttp(
          9000,
          "0.0.0.0"
        )
        .withHttpApp(api.httpApp)
        .serve
        .compile
        .drain
    } yield ExitCode.Success
}
