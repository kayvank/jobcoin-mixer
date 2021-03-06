package q2io.http.routes

import cats._
import org.http4s._
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import q2io.core.effects._
import q2io.http.DecoderR._
import q2io.domain.protocol.JsonCodecs._
import q2io.domain.model.Order._
import eu.timepit.refined.types.string.NonEmptyString
import q2io.domain.model.Account.MixerDepositAddress
import squants.market._
import q2io.domain.model.Account.PublicAddress

final class OrderRoutes[F[_]: Defer: JsonDecoder: MonadThrow]
    extends Http4sDsl[F] {

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "transaction" / "withdraw" / address =>
      req.decodeR[WithdrawOrderParam] { w =>
        val order = WithdrawOrder(
          orderId = OrderId(NonEmptyString("order-123")),
          amount = w.amount.toDamain,
          fee = BTC(w.amount.value.value * (0.005)),
          from = MixerDepositAddress(PublicAddress(address)),
          to = PublicAddress(w.to.value),
          ts = System.currentTimeMillis
        )
        Created(order)
      }

  }
  val routes = Router("/v1" -> httpRoutes)
}
