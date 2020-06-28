package cooking.chef

import akka.actor.typed.ActorRef
import cooking.manager.ManagerMsg.Reply

sealed trait ChefMsg

object ChefMsg {
  case object AreYouDone extends ChefMsg
  final case class AreYouDone(replyTo: ActorRef[Reply]) extends ChefMsg

  sealed trait Food extends ChefMsg { def servings: Int }
  final case class Ingredients(servings: Int) extends Food
  private[chef] final case class CookedFood(servings: Int) extends Food
  private[chef] final case class BurntFood(servings: Int) extends Food
}
