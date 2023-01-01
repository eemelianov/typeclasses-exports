package typeclasses.exports

object Main:

  def main(args: Array[String]): Unit =
    val res = foo()
    print(res)

  def foo[f[_]]()(using Monad: Monad[f]): f[Int] =
    for {
      a <- Monad.pure(10)
      b <- Monad.pure(20)
    } yield a + b

end Main


