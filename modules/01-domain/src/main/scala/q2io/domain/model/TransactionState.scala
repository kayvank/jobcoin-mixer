package q2io.domain.model

import enumeratum._
import enumeratum.EnumEntry.Lowercase

sealed trait TransactionState extends EnumEntry with Lowercase
case object TransactionState
    extends Enum[TransactionState]
    with CirisEnum[TransactionState] {
  case object Created extends TransactionState
  case object Submited extends TransactionState
  case object Completed extends TransactionState
  case object Error extends TransactionState
  val values = findValues
}
