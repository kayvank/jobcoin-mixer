package q2io.domain
package protocol

import cats._
import io.circe._
import io.circe.refined._
import io.circe.generic.semiauto._
import io.estatico.newtype.Coercible
import io.estatico.newtype.ops._
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

import q2io.domain.model._
import q2io.domain.model.Order._
import q2io.domain.model.Account._
import squants.market._

object JsonCodecs extends JsonCodecs {
  implicit def deriveEntityEncoder[F[_]: Applicative, A: Encoder]
      : EntityEncoder[F, A] = jsonEncoderOf[F, A]

}
trait JsonCodecs {
  // ----- Coercible codecs -----
  //
  implicit def coercibleDecoder[A: Coercible[B, *], B: Decoder]: Decoder[A] =
    Decoder[B].map(_.coerce[A])

  implicit def coercibleEncoder[A: Coercible[B, *], B: Encoder]: Encoder[A] =
    Encoder[B].contramap(_.repr.asInstanceOf[B])

  implicit def coercibleKeyDecoder[A: Coercible[B, *], B: KeyDecoder]
      : KeyDecoder[A] =
    KeyDecoder[B].map(_.coerce[A])

  implicit def coercibleKeyEncoder[A: Coercible[B, *], B: KeyEncoder]
      : KeyEncoder[A] =
    KeyEncoder[B].contramap[A](_.repr.asInstanceOf[B])

  // domain encode/decoders

  implicit val addressEncoder: Encoder[PublicAddress] =
    Encoder.forProduct1("address")(_.value)
  // deriveEncoder[Address]
  implicit val addressDecoder: Decoder[PublicAddress] =
    Decoder.forProduct1("address")(PublicAddress.apply)
  // deriveDecoder[Address]

  implicit val addressParamEncoder: Encoder[AddressParam] =
    Encoder.forProduct1("address")(_.value)
  // deriveEncoder[Address]
  implicit val addressParamDecoder: Decoder[AddressParam] =
    Decoder.forProduct1("address")(AddressParam.apply)
  // deriveDecoder[Address]

  implicit val registerUserAddressParamEncoder
      : Encoder[RegisterUserAddressParam] =
    deriveEncoder[RegisterUserAddressParam]
  implicit val registerUserAddressParamDecoder
      : Decoder[RegisterUserAddressParam] =
    deriveDecoder[RegisterUserAddressParam]

  implicit val userAccountDecoder: Decoder[UserAccount] =
    deriveDecoder[UserAccount]
  implicit val userAccountEncoder: Encoder[UserAccount] =
    deriveEncoder[UserAccount]

  implicit val orderStatusDecoder: Decoder[TransactionState] =
    Decoder[String].map(x => TransactionState.withNameInsensitive(x))
  implicit val orderStatusEncoder: Encoder[TransactionState] =
    Encoder[String].contramap(x => x.entryName)

  implicit val moneyDecoder: Decoder[Money] =
    Decoder[BigDecimal].map(BTC.apply)

  implicit val moneyEncoder: Encoder[Money] =
    Encoder[BigDecimal].contramap(_.amount)

  // case class Baa(amount: BigDecimal, currency: String)
  // implicit val bDecoder: Decoder[Baa] = deriveDecoder[Baa]
  // implicit val bEncoder: Encoder[Baa] = deriveEncoder[Baa]
  // implicit val moneyEncoder: Encoder[Money] =
  //   Encoder[Baa].contramap(x => Baa(x.amount.bigDecimal, "BTC"))

  implicit val orderDecoder: Decoder[MixerOrder] = deriveDecoder[MixerOrder]
  implicit val orderEncoder: Encoder[MixerOrder] = deriveEncoder[MixerOrder]

  implicit val withdrawOrderParamDecoder: Decoder[WithdrawOrderParam] =
    deriveDecoder[WithdrawOrderParam]
  implicit val withdrawOrderEncoderParam: Encoder[WithdrawOrderParam] =
    deriveEncoder[WithdrawOrderParam]

  implicit val withdrawOrderDecoder: Decoder[WithdrawOrder] =
    deriveDecoder[WithdrawOrder]
  implicit val withdrawOrderEncoder: Encoder[WithdrawOrder] =
    deriveEncoder[WithdrawOrder]

  /**
    */
  implicit val helathCheckDecoder: Decoder[HealthCheck] =
    deriveDecoder[HealthCheck]
  implicit val healthCheckEncoder: Encoder[HealthCheck] =
    deriveEncoder[HealthCheck]

}
