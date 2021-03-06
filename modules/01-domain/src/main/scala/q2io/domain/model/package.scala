package q2io.domain
package model

import cats.Eq
import cats.syntax.all._
import eu.timepit.refined._
import eu.timepit.refined.auto._
import eu.timepit.refined.collection.Size
import eu.timepit.refined.api.{Refined, Validate}
import org.http4s._
import io.estatico.newtype.Coercible
import io.estatico.newtype.ops._
import java.util.UUID

package object model {
  private def coercibleEq[A: Eq, B: Coercible[A, *]]: Eq[B] =
    new Eq[B] {
      def eqv(x: B, y: B): Boolean =
        Eq[A].eqv(x.repr.asInstanceOf[A], y.repr.asInstanceOf[A])
    }

  implicit def coercibleStringEq[B: Coercible[String, *]]: Eq[B] =
    coercibleEq[String, B]

  implicit def coercibleUuidEq[B: Coercible[UUID, *]]: Eq[B] =
    coercibleEq[UUID, B]

  implicit def coercibleIntEq[B: Coercible[Int, *]]: Eq[B] =
    coercibleEq[Int, B]

  implicit def coercibleQueryParamDecoder[A: Coercible[B, *], B: QueryParamDecoder]
      : QueryParamDecoder[A] =
    QueryParamDecoder[B].map(_.coerce[A])

  implicit def refinedQueryParamDecoder[T: QueryParamDecoder, P](
      implicit ev: Validate[T, P]
  ): QueryParamDecoder[T Refined P] =
    QueryParamDecoder[T].emap(refineV[P](_).leftMap(m => ParseFailure(m, m)))

  implicit def validateSizeN[N <: Int, R](
      implicit w: ValueOf[N]
  ): Validate.Plain[R, Size[N]] =
    Validate.fromPredicate[R, Size[N]](
      _.toString.size == w.value,
      _ => s"Must have ${w.value} digits",
      Size[N](w.value)
    )
}
