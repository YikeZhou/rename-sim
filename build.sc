import mill._
import mill.define.{Sources, Target}
import scalalib._
import scalafmt._

object renamesim extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.13.5"

  object test extends Tests {
    override def ivyDeps = Agg(ivy"org.scalatest::scalatest:3.2.2")
    def testFrameworks = Seq("org.scalatest.tools.Framework")
    def testOne(args: String*) = T.command {
      super.runMain("org.scalatest.run", args: _*)
    }
  }
}
