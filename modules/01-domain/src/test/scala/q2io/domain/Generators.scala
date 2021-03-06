package q2io.domain.model

// import Account._
// import Order._
import org.scalacheck._
import java.util.UUID
// import eu.timepit.refined.api.Refined
import io.estatico.newtype.ops._
import io.estatico.newtype.Coercible
import squants.market._
import q2io.domain.model.Order._
import q2io.domain.model.Account._
import eu.timepit.refined.types.string.NonEmptyString

object Generators {
  val genNonEmptyString: Gen[String] =
    Gen
      .choose(21, 40)
      .flatMap(n => Gen.buildableOfN[String, Char](n, Gen.alphaChar))
  def cbStr[A: Coercible[String, *]] = genNonEmptyString.map(_.coerce[A])

  def cbUUID[A: Coercible[UUID, *]]: Gen[A] = Gen.uuid.map(_.coerce[A])

  def cbInt[A: Coercible[Int, *]]: Gen[A] = Gen.posNum[Int].map(_.coerce[A])

  implicit def arbCoercibleUUID[A: Coercible[UUID, *]]: Arbitrary[A] =
    Arbitrary(cbUUID[A])

  implicit def arbCoercibleInt[A: Coercible[Int, *]]: Arbitrary[A] =
    Arbitrary(cbInt[A])

  implicit def arbCoercibleStr[A: Coercible[String, *]]: Arbitrary[A] =
    Arbitrary(cbStr[A])

  implicit def arbMoney: Arbitrary[Money] =
    Arbitrary(Gen.chooseNum(0.0, 100.00).map(BTC(_)))

  implicit def genOrderStat: Arbitrary[TransactionState] =
    Arbitrary(Gen.oneOf(TransactionState.values))

  implicit def arbOrder: Arbitrary[MixerOrder] = Arbitrary(orderGen)

  val orderGen: Gen[MixerOrder] = for {
    _transactionId <- cbUUID[TransactionId]
    _id = OrderId(NonEmptyString("tt"))
    _from <- cbStr[PublicAddress].map(MixerDepositAddress(_))
    _to <- cbStr[PublicAddress]
    _amount <- (Gen.chooseNum(0.0, 100.00).map(BTC(_)))
    _fee <- (Gen.chooseNum(0.0, 1.00).map(BTC(_)))
    _status <- Gen.oneOf(TransactionState.values)
  } yield MixerOrder(
    _id,
    _transactionId,
    _from,
    _to,
    _amount,
    _fee,
    _status
  )

}
