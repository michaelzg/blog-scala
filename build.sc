import mill._
import mill.api.Loose
import mill.scalalib.scalafmt.ScalafmtModule
import scalalib._

def akkaVersion = "2.6.3"
def akkaDeps: Loose.Agg[Dep] = Agg.from(
  List("akka-actor", "akka-actor-typed", "akka-stream").map { lib =>
    ivy"com.typesafe.akka::$lib:$akkaVersion"
  }
)

def commonDeps = Agg(
  ivy"com.typesafe.scala-logging::scala-logging:3.9.2",
  ivy"ch.qos.logback:logback-classic:1.2.3",
  ivy"joda-time:joda-time:2.10.5"
)

def testDeps = Agg(
  ivy"com.typesafe.akka::akka-actor-testkit-typed:$akkaVersion",
  ivy"org.scalatest::scalatest:3.1.2"
)

trait CommonModule extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.13.2"

  override def ivyDeps = akkaDeps ++ commonDeps

  object test extends Tests {
    def testFrameworks = Seq("org.scalatest.tools.Framework")

    override def ivyDeps = testDeps

    // Example: mill fsm.test.testOne cooking.chef.ChefBasicSpec
    def testOne(args: String*) = T.command {
      super.runMain("org.scalatest.run", args: _*)
    }
  }

}

object fsm extends CommonModule {
  override def mainClass = Some("CookingAppTyped")
}

object `streaming-merge` extends CommonModule
