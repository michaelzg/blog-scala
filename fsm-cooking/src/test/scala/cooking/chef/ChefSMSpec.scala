package cooking.chef

import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpecLike

class ChefSMSpec extends TestKit(ActorSystem()) with AnyFeatureSpecLike with GivenWhenThen {

  Feature("Testing Untyped FSM actor") {
    Scenario("Chef starts in the Plating state and receives BurntFood") {
      Given("Plating state")
      When("receiving BurntFood")
      Then("transitions back to cooking")
      // TODO
    }
  }

}
