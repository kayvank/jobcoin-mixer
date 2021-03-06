package q2io.domain.model

// import io.estatico.newtype.macros.newtype
import java.util.UUID
import squants.market._
import scala.util.control.NoStackTrace
import io.estatico.newtype.macros.newtype

import q2io.domain.model.Account._
import eu.timepit.refined.types.numeric.NonNegFloat
import java.util.Date
import eu.timepit.refined.types.string.NonEmptyString

object Order {

  case class TemporalOrderStatus(
      ts: EpocTime,
      status: TransactionState
  )

  case class TemporalOrderStatusTransition(
      from: TemporalOrderStatus,
      to: Option[TemporalOrderStatus]
  )

  type EpocTime = Long
  @newtype case class OrderId(value: NonEmptyString)
  @newtype case class TransactionId(value: UUID)
  @newtype case class CoinAmountParam(value: NonNegFloat) {
    def toDamain = BTC(value.value)
  }

  case class MixerOrder(
      id: OrderId,
      transactionId: TransactionId,
      from: MixerDepositAddress,
      to: PublicAddress,
      amount: Money,
      fee: Money,
      status: TransactionState
  )

  case class WithdrawOrderParam(
      to: NonEmptyString,
      amount: CoinAmountParam
  )

  case class WithdrawOrder(
      orderId: OrderId,
      amount: Money,
      fee: Money,
      from: MixerDepositAddress,
      to: PublicAddress,
      ts: EpocTime
  ) {
    def dateTimeStamp = new Date(ts)
  }

  case class DepositOrder(
      orderId: OrderId,
      amount: Money,
      from: MixerDepositAddress,
      to: PublicAddress,
      ts: EpocTime
  ) {
    def dateTimeStamp = new Date(ts)
  }

  case class InternalTransfer(from: MixerOrder, to: BigHouseAccount)
  case class InternalTransferParam(address: InternalTransfer, amount: Money)
  case class OrderError(cause: String) extends NoStackTrace
  case class TransactionError(cause: String) extends NoStackTrace
}
