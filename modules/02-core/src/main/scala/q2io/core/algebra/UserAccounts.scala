package q2io.core.algebra
import cats.effect._
import q2io.domain.model.Account._
import cats.syntax.all._
import q2io.core.effects.GenKeys._
import q2io.core.effects.GenKeys
// import dev.profunktor.redis4cats.RedisCommands

trait UserAccounts[F[_]] {
  def create(addresses: UserAddressesParam): F[UserAccount]
  def findUserAccountsBy(
      mixerAddress: MixerDepositAddress
  ): F[UserAccount]
}

object UserAccounts {
  def apply[F[_]: Sync]
  // (redis: RedisCommands[F, String, String])
      : F[UserAccounts[F]] = {
    Sync[F].delay(new UserAccounts[F] {
      override def create(
          addresses: UserAddressesParam
      ): F[UserAccount] =
        GenKeys[F].make
          .map(pk =>
            UserAccount(
              addresses.toDomain,
              MixerDepositAddress(pk.publicAddress)
            )
          )

      override def findUserAccountsBy(
          mixerAddress: MixerDepositAddress
      ): F[UserAccount] = ???

    })
  }
}
