package hello
/**
  * Created by shinya on 2016/01/10.
  */

import ai.Proto1
import scala.io.Source

object ChAIChAI {
  val name = "ChAIChAI"
  val proto =new Proto1

  def main(args: Array[String]) {
    println("Hello! I'm "+ name)
    val s = Source.stdin
    for (line <- s.getLines) {
      val msg = line
      println(prompt(name)+proto.dialogue(msg))
    }
  }

  def prompt(name:String): String = {
    return name + ">"
  }
}
