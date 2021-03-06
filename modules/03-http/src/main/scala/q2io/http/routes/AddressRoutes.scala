package q2io.http.routes

import cats._
import org.http4s._
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import q2io.core.effects._
import q2io.http.DecoderR._
import q2io.domain.model.Account._
import q2io.domain.protocol.JsonCodecs._

final class AddressRoutes[F[_]: Defer: JsonDecoder: MonadThrow]
    extends Http4sDsl[F] {

  private[routes] val prefixPath: String = "/v1"
  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "user" / "address" / address =>
//      Ok(UserAccount(AccountId(randomUUID()), List(Address(s"$parm $address"))))
      Ok(s"$address")
    case req @ POST -> Root / "user" / "register" =>
      req.decodeR[UserAddress] { u =>
        val pk = UserAccount(
          userAddresses = u.addresses,
          mixerDepositAddress =
            MixerDepositAddress(PublicAddress("test-address"))
        )
        Created(pk)
      // Created(UserAccount(uAs.toDomain, MixerDepositAddress(PublicAddress("test-public-address"))))

      }
  }
  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )

}
