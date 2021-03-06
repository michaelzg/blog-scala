package cooking.chef

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.typesafe.scalalogging.StrictLogging
import cooking.chef.ChefMsg._
import cooking.manager.ManagerMsg.Reply

import scala.util.{ Failure, Success }

class ChefTyped(customers: Int, skill: CookingSkill) extends StrictLogging {
  val behavior: Behavior[ChefMsg] = cooking(Data(served = 0))

  def cooking(data: Data): Behavior[ChefMsg] =
    Behaviors.receivePartial[ChefMsg] {
      case (ctx, ing @ Ingredients(servings)) =>
        logger.info(s"Cooking $servings servings.")
        skill
          .cook(ing)
          .onComplete {
            case Success(food) =>
              ctx.self ! food // safe with typed
            case Failure(ex) =>
              logger.warn(s"error while cooking with ${data.served} customers served: ${ex.getMessage}")
          }(ctx.executionContext)
        Behaviors.same

      case (ctx, cooked: CookedFood) =>
        ctx.self ! cooked
        plating(data)

      case (_, BurntFood(servings)) =>
        logger.warn(s"Burnt $servings servings.")
        cooking(data)

      case (_, AreYouDone(replyTo)) =>
        replyTo ! Reply(data.served, isDone = false)
        Behaviors.same
    }

  def plating(data: Data): Behavior[ChefMsg] =
    Behaviors.receiveMessagePartial[ChefMsg] {
      case CookedFood(servings) =>
        logger.info(s"Plating $servings servings.")
        val newData = Data(data.served + servings)
        if (newData.served >= customers) {
          logger.info("All fed.")
          done(newData)
        } else {
          val remaining = customers - newData.served
          logger.info(s"$remaining customers still hungry.")
          cooking(newData)
        }

      case _: BurntFood =>
        logger.warn("I will not plate this food!")
        cooking(data)

      case AreYouDone(replyTo) =>
        replyTo ! Reply(data.served, isDone = false)
        Behaviors.same
    }

  def done(data: Data): Behavior[ChefMsg] =
    Behaviors.receiveMessagePartial[ChefMsg] {
      case AreYouDone(replyTo) =>
        replyTo ! Reply(data.served, isDone = true)
        Behaviors.same
    }
}
