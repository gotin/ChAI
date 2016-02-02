package ai.responder

/**
  * Created by shinya on 2016/01/11.
  */

import scala.util.Random

class RandomResponder extends Responder {

  val responses = List("今日はさむいね", "チョコたべたい", "きのう10円ひろった")
  Random.setSeed(1)

  override def response(i: String): String = {
    val rand = Random.nextInt(responses.length)
    responses.apply(rand)
  }
}
