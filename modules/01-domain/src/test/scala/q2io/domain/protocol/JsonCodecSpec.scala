package q2io.domain
package protocol

import cats.data.NonEmptyList._
import io.circe.syntax._
import org.specs2._
import JsonCodecs._
import java.util.UUID.randomUUID
import squants.market._
import eu.timepit.refined.types.string.NonEmptyString

import q2io.domain.model.Generators._
import q2io.domain.model.TransactionState

import q2io.domain.model.Order._
import q2io.domain.model.Account._
import eu.timepit.refined.types.numeric.NonNegFloat

class JsonCodecSpec extends Specification with ScalaCheck {
  def is = s2"""
    JsonCodec spectications are:
        Order codes : $e1
    """

  def e1 = {

    val withdrawParam = WithdrawOrderParam(
      to = NonEmptyString("testpublicaddress"),
      amount = CoinAmountParam(NonNegFloat(3.98f))
    )
    println(s"withraw param = ${withdrawParam.asJson}")

    val userAddressesParam: UserAddressesParam = UserAddressesParam(
      fromList(
        List(
          AddressParam(NonEmptyString("1-address")),
          AddressParam(NonEmptyString("2-address")),
          AddressParam(NonEmptyString("3-address"))
        )
      ).get
    )
    val jsonUserAddressParam = userAddressesParam.asJson
    println(s"userAddressJson = ${jsonUserAddressParam}")

    val order = MixerOrder(
      OrderId(NonEmptyString("orderid123")),
      transactionId = TransactionId(randomUUID()),
      to = PublicAddress("toAddress"),
      from = MixerDepositAddress(PublicAddress("mixer-address")),
      amount = BTC(10),
      fee = BTC(1),
      status = TransactionState.Submited
    )
    println(s"json order is ${order.asJson}")
    order.asJson.as[MixerOrder] must beRight
  }

  def e2 = {
    prop((order: MixerOrder) => order.asJson.as[MixerOrder] must beRight)
  }
}
