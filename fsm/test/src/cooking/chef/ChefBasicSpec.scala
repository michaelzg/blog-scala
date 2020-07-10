package cooking.chef

import akka.actor.{ ActorRef, ActorSystem, Props }
import akka.pattern.ask
import akka.testkit.{ ImplicitSender, TestKit }
import akka.util.Timeout
import cooking.chef.ChefMsg.{ AreYouDone, Ingredients }
import cooking.chef.CookingSkill.DistractedNovice
import cooking.manager.ManagerMsg.Reply
import org.scalatest.GivenWhenThen
import org.scalatest.concurrent.Eventually
import org.scalatest.featurespec.AsyncFeatureSpecLike
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{ Millis, Span }

import scala.concurrent.duration._

class ChefBasicSpec
    extends TestKit(ActorSystem())
    with AsyncFeatureSpecLike
    with GivenWhenThen
    with Matchers
    with Eventually
    with ImplicitSender {

  implicit val patience = PatienceConfig(timeout = scaled(Span(450, Millis)), interval = scaled(Span(25, Millis)))

  val cookingSkill = DistractedNovice()

  val propsUntyped = Props(new Chef(10, cookingSkill))
  val chefUntyped = system.actorOf(propsUntyped)
  chefCooking(chefUntyped, "Untyped")

  val propsFSM = Props(new ChefSM(10, cookingSkill))
  val chefSM = system.actorOf(propsFSM)
  chefCooking(chefSM, "FSM")

  def chefCooking(chef: ActorRef, description: String) = {
    Feature(s"$description - A chef can cook food for customers") {
      Scenario("a chef given insufficient food will keep cooking") {
        Given("only an insufficient amount of ingredients")
        val ingredients = Ingredients(4)

        When("the chef cooks and plates it")
        chef ! ingredients

        Then("but the chef should remain cooking")
        implicit val timeout = Timeout(2.seconds)
        Thread.sleep(500)
        eventually {
          (chef ? AreYouDone).map { reply =>
            reply shouldBe Reply(served = 4, isDone = false)
          }
        }
      }
    }
  }
}
