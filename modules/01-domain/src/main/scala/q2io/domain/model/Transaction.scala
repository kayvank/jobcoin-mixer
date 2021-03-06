package q2io.domain.model

import q2io.domain.model.Account.AddressParam

import Order._

object Transaction {
  sealed trait Transaction {}
  case class WithdrawTransaction(
      id: TransactionId,
      witdraw: WithdrawOrder,
      temporalStatus: TemporalOrderStatus
  ) extends Transaction

  case class DepositTransaction(
      id: TransactionId,
      deposit: DepositOrder,
      temporalStatus: TemporalOrderStatus
  ) extends Transaction

  case class WithdrawParam(
      from: AddressParam,
      to: AddressParam,
      amount: CoinAmountParam
  )
}
