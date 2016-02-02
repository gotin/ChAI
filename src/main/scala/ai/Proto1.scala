package ai

/**
  * Created by shinya on 2016/01/11.
  */

import ai.responder._
import scala.util.Random

class Proto1 {

  Random.setSeed(1)

  private val respWhat = new WhatResponder
  private val respRand = new RandomResponder
  private var responder = new Responder

  def dialogue(i: String): String = {
    responder = if (Random.nextInt(2) == 0) respWhat else respRand
    responder.response(i)
  }
}

