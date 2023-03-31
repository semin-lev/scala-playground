package lev.test

import cats.effect.{IO, IOApp}
import cats.kernel.Monoid


given Monoid[Int] with
  def empty: Int = 0
  def combine(a: Int, b: Int): Int = a + b

given Monoid[String] with
  def empty: String = ""
  def combine(a: String, b: String): String = a + b

given [A: Monoid, B: Monoid]: Monoid[(A, B)] with
  def empty: (A, B) = (summon[Monoid[A]].empty, summon[Monoid[B]].empty)
  def combine(a: (A, B), b: (A, B)): (A, B) = (summon[Monoid[A]].combine(a._1, b._1), summon[Monoid[B]].combine(a._2, b._2))

def combine[A : Monoid](l: List[A]): A = l.foldRight(summon[Monoid[A]].empty)((a, b) => summon[Monoid[A]].combine(a, b))

object HelloTypeClasses extends IOApp.Simple {
  val run: IO[Unit] =
    for {
      _ <- IO(println(combine(List("1", "2", "3"))))
      _ <- IO(println(combine(List(1, 2, 3))))
      _ <- IO(println(combine(List((1, "1"), (2, "2"), (3, "3")))))
    } yield ()
}