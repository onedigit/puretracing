import cats.effect.IO

object Example {
  def main(args: Array[String]): Unit = {
    import puretracing.cats.noTracing._

    val algebra = new FooAlgebra(
      new BarAlgebra(
        new BazAlgebra[IO]
      ),
      new InstrumentedHttpClient[IO]
    )

    val app = algebra.foo().flatMap(Console[IO].println)

    app.unsafeRunSync()
  }
}
