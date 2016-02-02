package ai.responder

/**
  * Created by shinya on 2016/01/11.
  */
class WhatResponder extends Responder{

  override def response(i:String):String = s"$i ってなに?"

}
