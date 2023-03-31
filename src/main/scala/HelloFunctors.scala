package lev.test

import cats.effect.{IO, IOApp}
import scala.util.Either

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

given [A]: Functor[[B] =>> Either[A, B]] with
  def map[B, C](fa: Either[A, B])(f: B => C): Either[A, C] = fa match {
    case Right(b) => Right(f(b))
    case _ => fa.asInstanceOf[Either[A, C]]
  }

def mapToSomething[A, F[_]: Functor](v: F[A]): String = summon[Functor[F]].map(v)(_ => "Something").toString

object HelloFunctors extends IOApp.Simple {
  val x: Either[String, String] = Right("notsomething")
  val run: IO[Unit] =
    for {
      _ <- IO(println(mapToSomething(x)))
    } yield ()
}