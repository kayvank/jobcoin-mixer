package q2io.core.effects

import cats.effect._
import cats.syntax.all._
import java.util.Base64;
import java.security.{
  KeyPairGenerator,
  SecureRandom,
  KeyPair,
  PrivateKey,
  PublicKey
}
import q2io.domain.model.Account._
import cats.Monad

trait GenCryptoKey[F[_]] {
  def make: F[CryptoAddress]
}

object GenKeys {
  def apply[F[_]](implicit ev: GenCryptoKey[F]): GenCryptoKey[F] = ev

  implicit def syncGenCryptoKeys[F[_]: Sync: Monad]: GenCryptoKey[F] =
    new GenCryptoKey[F] {

      def make: F[CryptoAddress] = makeCryptoKeys.map(_.toDomain)

      private def makeCryptoKeys: F[CryptoKeys] = {
        val keyGen: KeyPairGenerator = KeyPairGenerator.getInstance("DSA")
        val random = SecureRandom.getInstance("SHA1PRNG")
        val _ = keyGen.initialize(1024, random)
        val kp: KeyPair = keyGen.generateKeyPair()
        val _privateKey: PrivateKey = kp.getPrivate()
        val _publicKey: PublicKey = kp.getPublic()
        val encoder = Base64.getEncoder
        Sync[F].delay(
          CryptoKeys(
            privateKey = encoder.encodeToString(_privateKey.getEncoded()),
            publicKey = encoder.encodeToString(_publicKey.getEncoded())
          )
        )
      }
    }
}
