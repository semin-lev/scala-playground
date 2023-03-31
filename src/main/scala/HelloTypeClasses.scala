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

trait Foldable[F[_]] {
  def foldRight[A](c: F[A], z: A)(op: (A, A) => A): A
}

given Foldable[List] with
  def foldRight[A](c: List[A], z: A)(op: (A, A) => A): A = c.foldRight(z)(op)

given Foldable[Set] with
  def foldRight[A](c: Set[A], z: A)(op: (A, A) => A): A = c.foldRight(z)(op)

def combine[A : Monoid, F[_]: Foldable](l: F[A]): A = summon[Foldable[F]].foldRight(l, summon[Monoid[A]].empty)((a, b) => summon[Monoid[A]].combine(a, b))

case class MyPair[A](_1: A, _2: A)
given Foldable[MyPair] with
  def foldRight[A](c: MyPair[A], z: A)(op: (A, A) => A): A = op(c._1, op(c._2, z))

object HelloTypeClasses extends IOApp.Simple {
  val run: IO[Unit] =
    for {
      _ <- IO(println(combine(List("1", "2", "3"))))
      _ <- IO(println(combine(List(1, 2, 3))))
      _ <- IO(println(combine(List((1, "1"), (2, "2"), (3, "3")))))
      _ <- IO(println(combine(Set(3, 4, 5))))
      _ <- IO(println(combine(MyPair(2, 33))))
      _ <- IO(println(combine(MyPair("3", "1"))))
    } yield ()
}