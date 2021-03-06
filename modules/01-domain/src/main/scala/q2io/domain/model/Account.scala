package q2io.domain.model

import io.estatico.newtype.macros.newtype
import java.util.UUID
import squants.market._
import scala.util.control.NoStackTrace
import cats.data.NonEmptyList
import eu.timepit.refined.types.string.NonEmptyString

object Account {
  @newtype case class TransferId(value: UUID)
  // @newtype case class AccountId(value: UUID)
  @newtype case class PublicAddress(value: String)
  @newtype case class PrivateAddress(value: String)
  case class CryptoKeys(privateKey: String, publicKey: String) {
    def toDomain = CryptoAddress(
      privateAddress = PrivateAddress(privateKey),
      publicAddress = PublicAddress(publicKey)
    )
  }
  case class CryptoAddress(
      privateAddress: PrivateAddress,
      publicAddress: PublicAddress
  )
  @newtype case class MixerDepositAddress(address: PublicAddress)
  @newtype case class UserAddress(addresses: List[PublicAddress])

  case class AddressParam(value: NonEmptyString) {
    def toDomain: PublicAddress = PublicAddress(value.value)
  }
  @newtype case class UserAddressesParam(value: NonEmptyList[AddressParam]) {
    def toDomain: List[PublicAddress] = value.map(_.toDomain).toList
  }

  case class RegisterUserAddressParam(value: List[PublicAddress])

  sealed trait Account

  case class UserAccount(
      userAddresses: List[PublicAddress],
      mixerDepositAddress: MixerDepositAddress
  ) extends Account

  case class BigHouseAccount(
      address: PublicAddress
  ) extends Account

  case class UserAccountNotFound(address: PublicAddress) extends NoStackTrace

  case class TransferToBigHouse(
      transferId: TransferId,
      mixerDepositAccount: PublicAddress,
      bigHouseAccount: PublicAddress,
      amount: Money
  )

}
