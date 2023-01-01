package typeclasses.exports

trait Functor[f[_]]:
  self =>
  def map[a, b](f: a => b): f[a] => f[b]

  extension[a] (fa: f[a])
    def map[b](f: a => b): f[b] = self.map(f)(fa)

end Functor

object Functor:
  given functorOption: Functor[Option] with
    def map[a, b](f: a => b): Option[a] => Option[b] = _.map(f)
end Functor


trait Applicative[f[_]](using functor: Functor[f]):

  export functor.*

  def ap[a, b](f: f[a => b]): f[a] => f[b]

  def pure[a](x: a): f[a]

end Applicative

object Applicative:
  given applicativeOption(using fa: Functor[Option]): Applicative[Option] with
    def ap[a, b](f: Option[a => b]): Option[a] => Option[b] = fa.map(f.get)

    def pure[a](x: a): Option[a] = Option(x)
end Applicative

trait Monad[f[_]](using applicative: Applicative[f]):
  self =>

  export applicative.*

  def flatMap[a, b](f: a => f[b]): f[a] => f[b]

  extension[a] (fa: f[a])
    def flatMap[b](f: a => f[b]): f[b] =
      self.flatMap(f)(fa)

end Monad

object Monad:
  given monadOption(using appl: Applicative[Option]): Monad[Option] with
    def flatMap[a, b](f: a => Option[b]): Option[a] => Option[b] = appl.ap(appl.pure(f))(_) match {
      case Some(Some(e)) => Some(e)
      case _ => None
    }

end Monad
