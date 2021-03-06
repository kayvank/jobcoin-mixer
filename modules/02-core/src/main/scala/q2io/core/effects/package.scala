package q2io.core

import cats.{ApplicativeError, MonadError}
import cats.effect._

package object effects {

  type BracketThrow[F[_]] = Bracket[F, Throwable]
  type AppThrow[F[_]] = ApplicativeError[F, Throwable]
  object AppThrow {
    def apply[F[_]](implicit ev: ApplicativeError[F, Throwable]): AppThrow[F] =
      ev
  }
  type MonadThrow[F[_]] = MonadError[F, Throwable]
  object MonadThrow {
    def apply[F[_]](implicit ev: MonadError[F, Throwable]): MonadThrow[F] = ev
  }

}
